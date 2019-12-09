package pl.credit.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.credit.model.Credit;


public interface CreditRepository extends JpaRepository<Credit, Long>{

}
