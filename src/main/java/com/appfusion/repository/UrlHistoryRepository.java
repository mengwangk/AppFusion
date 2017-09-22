package com.appfusion.repository;

import com.appfusion.domain.UrlHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the UrlHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UrlHistoryRepository extends JpaRepository<UrlHistory, Long> {

    @Query("select url_history from UrlHistory url_history where url_history.user.login = ?#{principal.username}")
    List<UrlHistory> findByUserIsCurrentUser();

}
