package com.appfusion.web.rest;

import com.appfusion.AppfusionApp;

import com.appfusion.domain.UrlHistory;
import com.appfusion.repository.UrlHistoryRepository;
import com.appfusion.repository.search.UrlHistorySearchRepository;
import com.appfusion.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.appfusion.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UrlHistoryResource REST controller.
 *
 * @see UrlHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppfusionApp.class)
public class UrlHistoryResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private UrlHistoryRepository urlHistoryRepository;

    @Autowired
    private UrlHistorySearchRepository urlHistorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUrlHistoryMockMvc;

    private UrlHistory urlHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UrlHistoryResource urlHistoryResource = new UrlHistoryResource(urlHistoryRepository, urlHistorySearchRepository);
        this.restUrlHistoryMockMvc = MockMvcBuilders.standaloneSetup(urlHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UrlHistory createEntity(EntityManager em) {
        UrlHistory urlHistory = new UrlHistory()
            .url(DEFAULT_URL)
            .dateCreated(DEFAULT_DATE_CREATED);
        return urlHistory;
    }

    @Before
    public void initTest() {
        urlHistorySearchRepository.deleteAll();
        urlHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createUrlHistory() throws Exception {
        int databaseSizeBeforeCreate = urlHistoryRepository.findAll().size();

        // Create the UrlHistory
        restUrlHistoryMockMvc.perform(post("/api/url-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(urlHistory)))
            .andExpect(status().isCreated());

        // Validate the UrlHistory in the database
        List<UrlHistory> urlHistoryList = urlHistoryRepository.findAll();
        assertThat(urlHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        UrlHistory testUrlHistory = urlHistoryList.get(urlHistoryList.size() - 1);
        assertThat(testUrlHistory.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testUrlHistory.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);

        // Validate the UrlHistory in Elasticsearch
        UrlHistory urlHistoryEs = urlHistorySearchRepository.findOne(testUrlHistory.getId());
        assertThat(urlHistoryEs).isEqualToComparingFieldByField(testUrlHistory);
    }

    @Test
    @Transactional
    public void createUrlHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = urlHistoryRepository.findAll().size();

        // Create the UrlHistory with an existing ID
        urlHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUrlHistoryMockMvc.perform(post("/api/url-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(urlHistory)))
            .andExpect(status().isBadRequest());

        // Validate the UrlHistory in the database
        List<UrlHistory> urlHistoryList = urlHistoryRepository.findAll();
        assertThat(urlHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = urlHistoryRepository.findAll().size();
        // set the field null
        urlHistory.setUrl(null);

        // Create the UrlHistory, which fails.

        restUrlHistoryMockMvc.perform(post("/api/url-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(urlHistory)))
            .andExpect(status().isBadRequest());

        List<UrlHistory> urlHistoryList = urlHistoryRepository.findAll();
        assertThat(urlHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = urlHistoryRepository.findAll().size();
        // set the field null
        urlHistory.setDateCreated(null);

        // Create the UrlHistory, which fails.

        restUrlHistoryMockMvc.perform(post("/api/url-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(urlHistory)))
            .andExpect(status().isBadRequest());

        List<UrlHistory> urlHistoryList = urlHistoryRepository.findAll();
        assertThat(urlHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUrlHistories() throws Exception {
        // Initialize the database
        urlHistoryRepository.saveAndFlush(urlHistory);

        // Get all the urlHistoryList
        restUrlHistoryMockMvc.perform(get("/api/url-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(urlHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(sameInstant(DEFAULT_DATE_CREATED))));
    }

    @Test
    @Transactional
    public void getUrlHistory() throws Exception {
        // Initialize the database
        urlHistoryRepository.saveAndFlush(urlHistory);

        // Get the urlHistory
        restUrlHistoryMockMvc.perform(get("/api/url-histories/{id}", urlHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(urlHistory.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.dateCreated").value(sameInstant(DEFAULT_DATE_CREATED)));
    }

    @Test
    @Transactional
    public void getNonExistingUrlHistory() throws Exception {
        // Get the urlHistory
        restUrlHistoryMockMvc.perform(get("/api/url-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUrlHistory() throws Exception {
        // Initialize the database
        urlHistoryRepository.saveAndFlush(urlHistory);
        urlHistorySearchRepository.save(urlHistory);
        int databaseSizeBeforeUpdate = urlHistoryRepository.findAll().size();

        // Update the urlHistory
        UrlHistory updatedUrlHistory = urlHistoryRepository.findOne(urlHistory.getId());
        updatedUrlHistory
            .url(UPDATED_URL)
            .dateCreated(UPDATED_DATE_CREATED);

        restUrlHistoryMockMvc.perform(put("/api/url-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUrlHistory)))
            .andExpect(status().isOk());

        // Validate the UrlHistory in the database
        List<UrlHistory> urlHistoryList = urlHistoryRepository.findAll();
        assertThat(urlHistoryList).hasSize(databaseSizeBeforeUpdate);
        UrlHistory testUrlHistory = urlHistoryList.get(urlHistoryList.size() - 1);
        assertThat(testUrlHistory.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testUrlHistory.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);

        // Validate the UrlHistory in Elasticsearch
        UrlHistory urlHistoryEs = urlHistorySearchRepository.findOne(testUrlHistory.getId());
        assertThat(urlHistoryEs).isEqualToComparingFieldByField(testUrlHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingUrlHistory() throws Exception {
        int databaseSizeBeforeUpdate = urlHistoryRepository.findAll().size();

        // Create the UrlHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUrlHistoryMockMvc.perform(put("/api/url-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(urlHistory)))
            .andExpect(status().isCreated());

        // Validate the UrlHistory in the database
        List<UrlHistory> urlHistoryList = urlHistoryRepository.findAll();
        assertThat(urlHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUrlHistory() throws Exception {
        // Initialize the database
        urlHistoryRepository.saveAndFlush(urlHistory);
        urlHistorySearchRepository.save(urlHistory);
        int databaseSizeBeforeDelete = urlHistoryRepository.findAll().size();

        // Get the urlHistory
        restUrlHistoryMockMvc.perform(delete("/api/url-histories/{id}", urlHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean urlHistoryExistsInEs = urlHistorySearchRepository.exists(urlHistory.getId());
        assertThat(urlHistoryExistsInEs).isFalse();

        // Validate the database is empty
        List<UrlHistory> urlHistoryList = urlHistoryRepository.findAll();
        assertThat(urlHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUrlHistory() throws Exception {
        // Initialize the database
        urlHistoryRepository.saveAndFlush(urlHistory);
        urlHistorySearchRepository.save(urlHistory);

        // Search the urlHistory
        restUrlHistoryMockMvc.perform(get("/api/_search/url-histories?query=id:" + urlHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(urlHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(sameInstant(DEFAULT_DATE_CREATED))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UrlHistory.class);
        UrlHistory urlHistory1 = new UrlHistory();
        urlHistory1.setId(1L);
        UrlHistory urlHistory2 = new UrlHistory();
        urlHistory2.setId(urlHistory1.getId());
        assertThat(urlHistory1).isEqualTo(urlHistory2);
        urlHistory2.setId(2L);
        assertThat(urlHistory1).isNotEqualTo(urlHistory2);
        urlHistory1.setId(null);
        assertThat(urlHistory1).isNotEqualTo(urlHistory2);
    }
}
