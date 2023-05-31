package ru.practicum.services;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.RequestDTO;
import ru.practicum.dto.RequestResponseDTO;
import ru.practicum.model.QRequest;
import ru.practicum.model.Request;
import ru.practicum.model.RequestMapper;
import ru.practicum.repositories.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    RequestRepository requestRepository;

    JPAQueryFactory queryFactory;

    @Override
    public RequestDTO save(RequestDTO requestDTO) {
        Request request = requestRepository.save(RequestMapper.requestFromRequestDTO(requestDTO));
        return RequestMapper.requestDTOFrom(request);
    }

    @Override
    public List<RequestResponseDTO> getStat(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {

        BooleanExpression query = QRequest.request.timestamp.between(start, end);

        if (uris != null) {
            query = query.and(QRequest.request.uri.in(uris));
        }

        NumberExpression<Long> ip;

        if (unique) {
            ip = QRequest.request.ip.countDistinct().as("hits");
        } else {
            ip = QRequest.request.ip.count().as("hits");
        }
        JPAQuery<RequestResponseDTO> jpaQuery = queryFactory.from(QRequest.request)
                .select(Projections.fields(RequestResponseDTO.class, QRequest.request.app, QRequest.request.uri, ip))
                .where(query)
                .groupBy(QRequest.request.app, QRequest.request.uri);

        return jpaQuery.fetch();

    }
}
