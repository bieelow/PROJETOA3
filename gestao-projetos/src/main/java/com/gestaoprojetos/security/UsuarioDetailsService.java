package com.gestaoprojetos.security;

import com.gestaoprojetos.models.Usuario;
import com.gestaoprojetos.repositories.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repo;

    public UsuarioDetailsService(UsuarioRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = repo.findAll().stream().filter(x -> username.equals(x.getLogin())).findFirst().orElse(null);
        if (u == null) throw new UsernameNotFoundException("Usuário não encontrado");
        GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_" + u.getPerfil().name());
        return new User(u.getLogin(), u.getSenha(), Collections.singleton(auth));
    }
}
