package ru.practicum.services;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.RequestDTO;
import ru.practicum.dto.RequestResponseDTO;
import ru.practicum.model.QRequest;
import ru.practicum.model.RequestMapper;
import ru.practicum.repositories.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private RequestRepository requestRepository;

    private JPAQueryFactory queryFactory;

    @Override
    public RequestDTO save(RequestDTO requestDTO) {
        return RequestMapper.requestDTOFrom(requestRepository.save(RequestMapper.requestFromRequestDTO(requestDTO)));
    }

    @Override
    public List<RequestResponseDTO> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        BooleanExpression query = QRequest.request.createdAt.between(start, end);

        if (uris != null) {
            query = query.and(QRequest.request.uri.in(uris));
        }

        NumberExpression<Long> ip = unique ? QRequest.request.ip.countDistinct() : QRequest.request.ip.count();

        return queryFactory.from(QRequest.request)
                .select(Projections.fields(RequestResponseDTO.class, QRequest.request.app, QRequest.request.uri, ip.as("hits")))
                .where(query)
                .groupBy(QRequest.request.app, QRequest.request.uri)
                .orderBy(ip.desc()).fetch();

    }
}
