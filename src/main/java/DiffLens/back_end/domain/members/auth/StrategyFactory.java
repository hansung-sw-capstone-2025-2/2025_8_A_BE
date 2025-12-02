package DiffLens.back_end.domain.members.auth;

import DiffLens.back_end.domain.members.auth.strategy.implement.AuthGeneralStrategy;
import DiffLens.back_end.domain.members.auth.strategy.implement.AuthGoogleStrategy;
import DiffLens.back_end.domain.members.auth.strategy.interfaces.AuthStrategy;
import DiffLens.back_end.domain.members.enums.LoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StrategyFactory {

    private final AuthGeneralStrategy generalStrategy;
    private final AuthGoogleStrategy googleStrategy;

    public AuthStrategy getStrategy(LoginType loginType) {
        return switch (loginType) {
            case LoginType.GENERAL -> generalStrategy;
            case LoginType.GOOGLE -> googleStrategy;
            // 추가하면 됨
        };
    }
}