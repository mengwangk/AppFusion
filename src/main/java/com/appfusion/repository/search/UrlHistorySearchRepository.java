package com.appfusion.repository.search;

import com.appfusion.domain.UrlHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UrlHistory entity.
 */
public interface UrlHistorySearchRepository extends ElasticsearchRepository<UrlHistory, Long> {
}
