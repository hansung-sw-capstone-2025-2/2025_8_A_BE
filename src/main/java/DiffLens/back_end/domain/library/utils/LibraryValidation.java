package DiffLens.back_end.domain.library.utils;

import DiffLens.back_end.domain.library.entity.Library;
import DiffLens.back_end.domain.members.entity.Member;
import DiffLens.back_end.global.responses.code.status.error.ErrorStatus;
import DiffLens.back_end.global.responses.exception.handler.ErrorHandler;

public class LibraryValidation {

    /**
     *
     * 라이브러리의 주인이 맞는지 확인합니다
     * member가 라이브러리의 주인이 아니라면 예외를 발생시킴니다.
     *
     * @param library 체크할 라이브러리
     * @param member Member
     */
    public static boolean checkIsMyLibrary(Library library, Member member) {
        if (!library.getMember().getId().equals(member.getId())) {
            throw new ErrorHandler(ErrorStatus.FORBIDDEN);
        }
        return true;
    }

}
