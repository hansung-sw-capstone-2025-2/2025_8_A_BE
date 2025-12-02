package DiffLens.back_end.global.util.data.initializer;

import DiffLens.back_end.domain.members.entity.Plan;
import DiffLens.back_end.domain.members.enums.PlanEnum;
import DiffLens.back_end.domain.members.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * PlanEnum에 있는 값들 DB에 적용
 */
@Component
@RequiredArgsConstructor
public class PlanInitializer implements  Initializer {

    private final PlanRepository planRepository;

    @Override
    @Transactional
    public void initialize() {

        Arrays.stream(PlanEnum.values())
                .forEach(planEnum ->
                    planRepository.save(
                            Plan.builder()
                                    .id(planEnum.getId())
                                    .name(planEnum.getName())
                                    .price(planEnum.getPrice())
                                    .build()
                    )
                );

    }


}
