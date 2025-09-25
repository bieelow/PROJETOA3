/* Decide backend mode: REST (default) or localStorage (fallback) */
const USE_API = true;
const API = { users: '/api/users', projects: '/api/projects', teams: '/api/teams' };
const dbFallback = {
  get(key) { return JSON.parse(localStorage.getItem(key) || "[]"); },
  set(key, data) { localStorage.setItem(key, JSON.stringify(data)); },
  uid() { return '_' + Math.random().toString(36).substr(2, 9); }
};
const DB_KEYS = { users:'pm_users', projects:'pm_projects', teams:'pm_teams' };

/* API helpers */
async function apiList(url){ const r = await fetch(url); if(!r.ok) throw new Error('API list failed'); return r.json(); }
async function apiCreate(url, data){ const r = await fetch(url, {method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(data)}); if(!r.ok) throw new Error('API create failed'); return r.json(); }
async function apiUpdate(url, id, data){ const r = await fetch(url+'/'+id, {method:'PUT', headers:{'Content-Type':'application/json'}, body:JSON.stringify(data)}); if(!r.ok) throw new Error('API update failed'); return r.json(); }
async function apiDelete(url, id){ const r = await fetch(url+'/'+id, {method:'DELETE'}); if(!r.ok) throw new Error('API delete failed'); }

/* UI helpers */
function maskCPF(value) {
  return value.replace(/\D/g, '').replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d{1,2})$/, '$1-$2').slice(0, 14);
}
function isValidCPFFormat(v){ return /^\d{3}\.\d{3}\.\d{3}-\d{2}$/.test(v); }
function isValidEmail(v){ return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test((v||'').trim()); }
function roleBadge(role){ const map={administrador:'danger',gerente:'info',colaborador:'secondary'}; return `<span class="badge badge-${map[role]||'light'} badge-role">${role||''}</span>`; }
function statusBadge(st){
  const map={planejado:'secondary',em_andamento:'info',concluido:'success',cancelado:'danger'};
  const label={planejado:'Planejado', em_andamento:'Em andamento', concluido:'Concluído', cancelado:'Cancelado'}[st]||st;
  return `<span class="badge badge-${map[st]||'light'}">${label}</span>`;
}
function ensureInvalidFeedback($input, message){ let $fb=$input.siblings(".invalid-feedback"); if(!$fb.length){ $fb=$('<div class="invalid-feedback"></div>'); $input.after($fb);} if(message) $fb.text(message); }
function toggleValidity($input, ok, message){ ensureInvalidFeedback($input,message); if(ok){$input.removeClass("is-invalid").addClass("is-valid");} else {$input.removeClass("is-valid").addClass("is-invalid");} }
function findById(list, id){ return (list||[]).find(x=>x.id===id); }

/* Global cache */
let cache = { users:[], projects:[], teams:[] };

async function loadCache(){
  try{
    cache.users = await apiList(API.users);
    cache.projects = await apiList(API.projects);
    cache.teams = await apiList(API.teams);
  } catch(e){
    console.warn("API not available, falling back to localStorage", e);
    cache.users = dbFallback.get(DB_KEYS.users);
    cache.projects = dbFallback.get(DB_KEYS.projects);
    cache.teams = dbFallback.get(DB_KEYS.teams);
  }
}

/* USERS */
async function initUsersPage(){
  await loadCache();
  const $tbody = $("#tableUsers tbody");

  function render(){
    $tbody.empty();
    cache.users.forEach(u=>{
      $tbody.append(`
        <tr>
          <td>${u.name}</td><td>${u.cpf}</td><td>${u.email}</td><td>${u.job||'-'}</td>
          <td>${roleBadge(u.role)}</td><td>${u.login}</td>
          <td class="text-right">
            <button class="btn btn-sm btn-outline-primary btn-edit" data-id="${u.id}"><i class="fas fa-edit"></i></button>
            <button class="btn btn-sm btn-outline-danger btn-del" data-id="${u.id}"><i class="fas fa-trash"></i></button>
          </td>
        </tr>`);
    });
  }

  const $cpf = $("#userCPF"); const $email = $("#userEmail");
  $cpf.on("input", function(){ this.value = maskCPF(this.value); });
  $cpf.on("blur", function(){ toggleValidity($($cpf), !this.value || isValidCPFFormat(this.value), "CPF inválido."); });
  $email.on("input blur", function(){ toggleValidity($($email), isValidEmail(this.value), "E-mail inválido."); });

  $("#formUser").on("submit", async function(e){
    e.preventDefault();
    const payload = {
      id: $("#userId").val(),
      name: $("#userName").val().trim(),
      cpf: $("#userCPF").val().trim(),
      email: $("#userEmail").val().trim(),
      job: $("#userJobTitle").val().trim(),
      role: $("#userRole").val(),
      login: $("#userLogin").val().trim(),
      password: $("#userPassword").val()
    };
    if(!payload.name || !payload.cpf || !payload.email || !payload.role || !payload.login || !payload.password){
      return alert("Preencha os campos obrigatórios."); }

    try{
      if(cache.users.find(u=>u.id===payload.id)){ await apiUpdate(API.users, payload.id, payload); }
      else { const created = await apiCreate(API.users, payload); payload.id = created.id; }
      cache.users = await apiList(API.users);
    } catch(e){
      const list = dbFallback.get(DB_KEYS.users);
      if(payload.id){ const idx = list.findIndex(x=>x.id===payload.id); if(idx>=0) list[idx]=payload; }
      else { payload.id = dbFallback.uid(); list.push(payload); }
      dbFallback.set(DB_KEYS.users, list); cache.users = list;
    }

    $("#userModal").modal("hide");
    this.reset(); $("#userId").val(""); $(".is-valid, .is-invalid").removeClass("is-valid is-invalid");
    render(); refreshManagersSelects(); refreshMembersProjectsSelects();
  });

  $tbody.on("click",".btn-edit", function(){
    const id=$(this).data("id"); const u = findById(cache.users, id); if(!u) return;
    $("#userModalLabel").text("Editar Usuário");
    $("#userId").val(u.id); $("#userName").val(u.name); $("#userCPF").val(u.cpf);
    $("#userEmail").val(u.email); $("#userJobTitle").val(u.job); $("#userRole").val(u.role);
    $("#userLogin").val(u.login); $("#userPassword").val(u.password);
    $(".is-valid, .is-invalid").removeClass("is-valid is-invalid"); $("#userModal").modal("show");
  });

  $tbody.on("click",".btn-del", async function(){
    const id=$(this).data("id");
    if(confirm("Excluir este usuário?")){
      try{ await apiDelete(API.users, id); cache.users = await apiList(API.users); }
      catch(e){ const list=dbFallback.get(DB_KEYS.users).filter(x=>x.id!==id); dbFallback.set(DB_KEYS.users, list); cache.users = list; }
      render(); refreshManagersSelects(); refreshMembersProjectsSelects();
    }
  });

  $("#userModal").on("hidden.bs.modal", function(){ $("#formUser")[0].reset(); $("#userId").val(""); $("#userModalLabel").text("Novo Usuário"); $(".is-valid, .is-invalid").removeClass("is-valid is-invalid"); });

  render();
}

/* PROJECTS */
function refreshManagersSelects(){
  const $mgr = $("#projectManager");
  if(!$mgr.length) return;
  $mgr.empty().append(`<option value="">Selecione...</option>`);
  cache.users.forEach(u=>{
    const label = u.role === "gerente" ? `${u.name} (Gerente)` : u.name;
    $mgr.append(`<option value="${u.id}">${label}</option>`);
  });
}
async function initProjectsPage(){
  await loadCache();
  const $tbody = $("#tableProjects tbody");

  function render(){
    $tbody.empty();
    cache.projects.forEach(p=>{
      const manager = findById(cache.users, p.managerId);
      $tbody.append(`
        <tr>
          <td>${p.name}</td><td>${p.desc}</td><td>${p.start}</td><td>${p.end}</td>
          <td>${statusBadge(p.status)}</td><td>${manager?manager.name:'-'}</td>
          <td class="text-right">
            <button class="btn btn-sm btn-outline-primary btn-edit" data-id="${p.id}"><i class="fas fa-edit"></i></button>
            <button class="btn btn-sm btn-outline-danger btn-del" data-id="${p.id}"><i class="fas fa-trash"></i></button>
          </td>
        </tr>`);
    });
  }

  refreshManagersSelects();

  $("#formProject").on("submit", async function(e){
    e.preventDefault();
    const payload = {
      id: $("#projectId").val(),
      name: $("#projectName").val().trim(),
      desc: $("#projectDesc").val().trim(),
      start: $("#projectStart").val(),
      end: $("#projectEnd").val(),
      status: $("#projectStatus").val(),
      managerId: $("#projectManager").val()
    };
    if(!payload.name || !payload.desc || !payload.start || !payload.end || !payload.status || !payload.managerId){
      ["#projectName","#projectDesc","#projectStart","#projectEnd","#projectStatus","#projectManager"].forEach(sel=>{
        const $el=$(sel); const ok=!!$el.val(); toggleValidity($el, ok, "Campo obrigatório.");
      });
      return;
    }
    try{
      if(cache.projects.find(p=>p.id===payload.id)){ await apiUpdate(API.projects, payload.id, payload); }
      else { const created = await apiCreate(API.projects, payload); payload.id = created.id; }
      cache.projects = await apiList(API.projects);
    } catch(e){
      const list=dbFallback.get(DB_KEYS.projects);
      if(payload.id){ const idx=list.findIndex(x=>x.id===payload.id); if(idx>=0) list[idx]=payload; }
      else { payload.id = dbFallback.uid(); list.push(payload); }
      dbFallback.set(DB_KEYS.projects, list); cache.projects = list;
    }
    $("#projectModal").modal("hide");
    this.reset(); $("#projectId").val(""); $(".is-valid, .is-invalid").removeClass("is-valid is-invalid");
    render(); refreshMembersProjectsSelects();
  });

  $tbody.on("click",".btn-edit", function(){
    const id=$(this).data("id"); const p=findById(cache.projects, id); if(!p) return;
    $("#projectModalLabel").text("Editar Projeto");
    $("#projectId").val(p.id); $("#projectName").val(p.name); $("#projectDesc").val(p.desc);
    $("#projectStart").val(p.start); $("#projectEnd").val(p.end); $("#projectStatus").val(p.status);
    refreshManagersSelects(); $("#projectManager").val(p.managerId);
    $(".is-valid, .is-invalid").removeClass("is-valid is-invalid"); $("#projectModal").modal("show");
  });

  $tbody.on("click",".btn-del", async function(){
    const id=$(this).data("id");
    if(confirm("Excluir este projeto?")){
      try{ await apiDelete(API.projects, id); cache.projects = await apiList(API.projects); }
      catch(e){ const list=dbFallback.get(DB_KEYS.projects).filter(x=>x.id!==id); dbFallback.set(DB_KEYS.projects, list); cache.projects = list; }
      render(); refreshMembersProjectsSelects();
    }
  });

  $("#projectModal").on("hidden.bs.modal", function(){ $("#formProject")[0].reset(); $("#projectId").val(""); $("#projectModalLabel").text("Novo Projeto"); $(".is-valid, .is-invalid").removeClass("is-valid is-invalid"); });

  render();
}

/* TEAMS */
function refreshMembersProjectsSelects(){
  const $members=$("#teamMembers"); const $projs=$("#teamProjects");
  if($members.length){ $members.empty(); cache.users.forEach(u=>$members.append(`<option value="${u.id}">${u.name} - ${u.role}</option>`)); }
  if($projs.length){ $projs.empty(); cache.projects.forEach(p=>$projs.append(`<option value="${p.id}">${p.name}</option>`)); }
}
async function initTeamsPage(){
  await loadCache();
  const $tbody = $("#tableTeams tbody");

  function render(){
    $tbody.empty();
    cache.teams.forEach(t=>{
      const members=(t.memberIds||[]).map(id=>(findById(cache.users,id)||{}).name||'—').join(", ");
      const projects=(t.projectIds||[]).map(id=>(findById(cache.projects,id)||{}).name||'—').join(", ");
      $tbody.append(`
        <tr>
          <td>${t.name}</td><td>${t.desc||'-'}</td><td>${members||'-'}</td><td>${projects||'-'}</td>
          <td class="text-right">
            <button class="btn btn-sm btn-outline-primary btn-edit" data-id="${t.id}"><i class="fas fa-edit"></i></button>
            <button class="btn btn-sm btn-outline-danger btn-del" data-id="${t.id}"><i class="fas fa-trash"></i></button>
          </td>
        </tr>`);
    });
  }

  refreshMembersProjectsSelects();

  $("#formTeam").on("submit", async function(e){
    e.preventDefault();
    const payload = {
      id: $("#teamId").val(),
      name: $("#teamName").val().trim(),
      desc: $("#teamDesc").val().trim(),
      memberIds: $("#teamMembers").val() || [],
      projectIds: $("#teamProjects").val() || []
    };
    const nameOk=!!payload.name; toggleValidity($("#teamName"), nameOk, "Informe o nome da equipe.");
    const membersOk=(payload.memberIds||[]).length>0; toggleValidity($("#teamMembers"), membersOk, "Selecione ao menos um membro.");
    if(!nameOk || !membersOk) return;

    try{
      if(cache.teams.find(t=>t.id===payload.id)){ await apiUpdate(API.teams, payload.id, payload); }
      else { const created = await apiCreate(API.teams, payload); payload.id = created.id; }
      cache.teams = await apiList(API.teams);
    } catch(e){
      const list=dbFallback.get(DB_KEYS.teams);
      if(payload.id){ const idx=list.findIndex(x=>x.id===payload.id); if(idx>=0) list[idx]=payload; }
      else { payload.id = dbFallback.uid(); list.push(payload); }
      dbFallback.set(DB_KEYS.teams, list); cache.teams = list;
    }
    $("#teamModal").modal("hide");
    this.reset(); $("#teamId").val(""); $(".is-valid, .is-invalid").removeClass("is-valid is-invalid");
    render();
  });

  $tbody.on("click",".btn-edit", function(){
    const id=$(this).data("id"); const t=findById(cache.teams,id); if(!t) return;
    $("#teamModalLabel").text("Editar Equipe");
    $("#teamId").val(t.id); $("#teamName").val(t.name); $("#teamDesc").val(t.desc);
    refreshMembersProjectsSelects(); $("#teamMembers").val(t.memberIds); $("#teamProjects").val(t.projectIds);
    $(".is-valid, .is-invalid").removeClass("is-valid is-invalid"); $("#teamModal").modal("show");
  });

  $tbody.on("click",".btn-del", async function(){
    const id=$(this).data("id");
    if(confirm("Excluir esta equipe?")){
      try{ await apiDelete(API.teams, id); cache.teams = await apiList(API.teams); }
      catch(e){ const list=dbFallback.get(DB_KEYS.teams).filter(x=>x.id!==id); dbFallback.set(DB_KEYS.teams, list); cache.teams = list; }
      render();
    }
  });

  $("#teamModal").on("hidden.bs.modal", function(){ $("#formTeam")[0].reset(); $("#teamId").val(""); $("#teamModalLabel").text("Nova Equipe"); $(".is-valid, .is-invalid").removeClass("is-valid is-invalid"); });

  render();
}

/* Router + global search */
$(function(){
  const path = location.pathname;
  if(path.endsWith("users.html")) initUsersPage();
  if(path.endsWith("projects.html")) initProjectsPage();
  if(path.endsWith("teams.html")) initTeamsPage();
  $("#globalSearch").on("input", function(){
    const q = $(this).val().toLowerCase();
    $("table tbody tr").each(function(){
      const txt = $(this).text().toLowerCase();
      $(this).toggle(txt.indexOf(q) >= 0);
    });
  });
});
