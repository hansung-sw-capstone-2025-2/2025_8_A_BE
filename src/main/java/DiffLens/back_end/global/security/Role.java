package DiffLens.back_end.global.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum Role implements GrantedAuthority {

    ROLE_USER,
    ROLE_ADMIN,
    ;

    @Override
    public String getAuthority() {
        return name();
    }

}
