package com.example.gitminer.repository;

import com.example.gitminer.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, String> {
    List<Issue> findByState(String state); // <- not 'status'
}
