package com.appfusion.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appfusion.domain.UrlHistory;
import com.appfusion.repository.UrlHistoryRepository;
import com.appfusion.repository.search.UrlHistorySearchRepository;
import com.appfusion.security.SecurityUtils;
import com.appfusion.web.rest.util.HeaderUtil;
import com.appfusion.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing UrlHistory.
 */
@RestController
@RequestMapping("/api")
public class UrlHistoryResource {

    private final Logger log = LoggerFactory.getLogger(UrlHistoryResource.class);

    private static final String ENTITY_NAME = "urlHistory";

    private final UrlHistoryRepository urlHistoryRepository;

    private final UrlHistorySearchRepository urlHistorySearchRepository;

    public UrlHistoryResource(UrlHistoryRepository urlHistoryRepository, UrlHistorySearchRepository urlHistorySearchRepository) {
        this.urlHistoryRepository = urlHistoryRepository;
        this.urlHistorySearchRepository = urlHistorySearchRepository;
    }

    /**
     * POST  /url-histories : Create a new urlHistory.
     *
     * @param urlHistory the urlHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new urlHistory, or with status 400 (Bad Request) if the urlHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/url-histories")
    @Timed
    public ResponseEntity<UrlHistory> createUrlHistory(@Valid @RequestBody UrlHistory urlHistory) throws URISyntaxException {
        log.debug("REST request to save UrlHistory : {}", urlHistory);
        if (urlHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new urlHistory cannot already have an ID")).body(null);
        }
        UrlHistory result = urlHistoryRepository.save(urlHistory);
        urlHistorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/url-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /url-histories : Updates an existing urlHistory.
     *
     * @param urlHistory the urlHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated urlHistory,
     * or with status 400 (Bad Request) if the urlHistory is not valid,
     * or with status 500 (Internal Server Error) if the urlHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/url-histories")
    @Timed
    public ResponseEntity<UrlHistory> updateUrlHistory(@Valid @RequestBody UrlHistory urlHistory) throws URISyntaxException {
        log.debug("REST request to update UrlHistory : {}", urlHistory);
        if (urlHistory.getId() == null) {
            return createUrlHistory(urlHistory);
        }
        UrlHistory result = urlHistoryRepository.save(urlHistory);
        urlHistorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, urlHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /url-histories : get all the urlHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of urlHistories in body
     */
    @GetMapping("/url-histories")
    @Timed
    public ResponseEntity<List<UrlHistory>> getAllUrlHistories(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of UrlHistories");

        //Page<UrlHistory> page = urlHistoryRepository.findAll(pageable);
        Page<UrlHistory> page = urlHistoryRepository.findByUserLoginOrderByDateCreatedDesc(SecurityUtils.getCurrentUserLogin(), pageable);
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/url-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /url-histories/:id : get the "id" urlHistory.
     *
     * @param id the id of the urlHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the urlHistory, or with status 404 (Not Found)
     */
    @GetMapping("/url-histories/{id}")
    @Timed
    public ResponseEntity<UrlHistory> getUrlHistory(@PathVariable Long id) {
        log.debug("REST request to get UrlHistory : {}", id);
        UrlHistory urlHistory = urlHistoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(urlHistory));
    }

    /**
     * DELETE  /url-histories/:id : delete the "id" urlHistory.
     *
     * @param id the id of the urlHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/url-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteUrlHistory(@PathVariable Long id) {
        log.debug("REST request to delete UrlHistory : {}", id);
        urlHistoryRepository.delete(id);
        urlHistorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/url-histories?query=:query : search for the urlHistory corresponding
     * to the query.
     *
     * @param query the query of the urlHistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/url-histories")
    @Timed
    public ResponseEntity<List<UrlHistory>> searchUrlHistories(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of UrlHistories for query {}", query);
        Page<UrlHistory> page = urlHistorySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/url-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
