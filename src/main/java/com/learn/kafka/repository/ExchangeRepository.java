package com.learn.kafka.repository;

import com.learn.kafka.model.ExchangeModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends ElasticsearchRepository<ExchangeModel, String> {
}
