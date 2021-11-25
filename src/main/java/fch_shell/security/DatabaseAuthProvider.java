package fch_shell.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import fch_shell.dal.models.Usuario;
import fch_shell.dal.repositories.UsuariosRepository;

public class DatabaseAuthProvider implements AuthenticationProvider {

	@Resource
	private UsuariosRepository usuariosRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String correo = authentication.getName();
		final String password = authentication.getCredentials().toString();
		final String salt = "$2a$12$";
		final Usuario usuarioAuth = usuariosRepository.findByCorreo(correo);
		
		if(usuarioAuth==null){
			//Aquí podríamos enviar aviso de que el usuario no existe/está invalidado(cambiando la query)
			throw new BadCredentialsException("Usuario o contraseña no válidos.");
		}else {
			if(BCrypt.checkpw(password, salt.concat(usuarioAuth.getPassword()))) {
				if(usuarioAuth.getUltimaConexion()!=null) {
					ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					HttpSession session = attr.getRequest().getSession(false);
			        session.setAttribute("KEY_TENANTID_SESSION", usuarioAuth.getUltimaConexion());
				}
				List<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority("ROLE_"+usuarioAuth.getPerfil()));
				return new UsernamePasswordAuthenticationToken(correo, password, authorities);
			} else {
				//aquí se llega solo si la contraseña es incorrecta
				throw new BadCredentialsException("Usuario o contraseña no válidos.");
			}
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(
				UsernamePasswordAuthenticationToken.class);
	}
	
}
