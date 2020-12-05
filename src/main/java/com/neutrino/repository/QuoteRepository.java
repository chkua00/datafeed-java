package com.neutrino.repository;

import com.neutrino.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, String> {

}
