package DiffLens.back_end.domain.members.service.auth;

import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.global.responses.code.status.error.AuthStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public Member getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ErrorHandler(AuthStatus.AUTHENTICATION_FAILED);
        }

        var principal = authentication.getPrincipal();
        if (principal == null) {
            throw new ErrorHandler(AuthStatus.AUTHENTICATION_FAILED);
        }

        if (!(principal instanceof Member)) {
            throw new ErrorHandler(AuthStatus.AUTHENTICATION_FAILED);
        }

        return (Member) principal;
    }

    public Long getCurrentUserId() {
        Member user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    public String getCurrentUserEmail() {
        Member user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }


}
