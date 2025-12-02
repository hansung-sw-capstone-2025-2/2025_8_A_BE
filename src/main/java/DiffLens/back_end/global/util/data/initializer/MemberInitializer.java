package DiffLens.back_end.global.util.data.initializer;

import DiffLens.back_end.domain.members.auth.strategy.implement.AuthAdminStrategy;
import DiffLens.back_end.domain.members.dto.auth.AuthRequestDTO;
import DiffLens.back_end.domain.members.enums.Industry;
import DiffLens.back_end.domain.members.enums.Job;
import DiffLens.back_end.domain.members.enums.LoginType;
import DiffLens.back_end.domain.members.enums.PlanEnum;
import DiffLens.back_end.domain.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberInitializer implements Initializer {

    private final AuthAdminStrategy authStrategy;
    private final MemberRepository memberRepository;

    @Value("${auth.admin_email}")
    private String EMAIL;

    @Value("${auth.admin_pw}")
    private String PASSWORD;

    @Override
    public void initialize() {

        Boolean isExist = memberRepository.existsByEmail(EMAIL);

        if(!isExist){
            AuthRequestDTO.SignUp admin = new AuthRequestDTO.SignUp(
                    EMAIL,
                    "admin",
                    PASSWORD,
                    LoginType.GENERAL,
                    PlanEnum.BUSINESS,
                    Job.ETC_FREELANCER,
                    Industry.ETC
            );
            authStrategy.signUp(admin);
        }

    }
}
