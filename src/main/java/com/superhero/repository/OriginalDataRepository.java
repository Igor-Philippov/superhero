package com.superhero.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.superhero.model.OriginalData;

public interface OriginalDataRepository extends PagingAndSortingRepository<OriginalData, Integer> {
}
