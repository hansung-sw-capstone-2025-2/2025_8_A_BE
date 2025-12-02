package DiffLens.back_end.domain.search.converter;

/**
 * 검색 관련 DTO에 관한 converter
 *
 * T : 클라이언트 -> 서버 DTO
 * R : fast api -> spring boot 웅덥 DTO
 * S : 반환할 DTO
 */
public interface SearchDtoConverter<R, S, T> {
    public S requestToDto(R response, T info);
}
