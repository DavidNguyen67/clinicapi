package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.LoyaltyTransaction;
import com.clinicsystem.clinicapi.model.LoyaltyTransaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, UUID> {

    List<LoyaltyTransaction> findByPatientId(UUID patientId);

    List<LoyaltyTransaction> findByTransactionType(TransactionType transactionType);

    @Query("SELECT lt FROM LoyaltyTransaction lt WHERE lt.patient.id = :patientId " +
            "ORDER BY lt.createdAt DESC")
    List<LoyaltyTransaction> findByPatientIdOrderByDateDesc(@Param("patientId") UUID patientId);

    @Query("SELECT SUM(lt.points) FROM LoyaltyTransaction lt WHERE lt.patient.id = :patientId " +
            "AND lt.transactionType = 'earn' AND (lt.expiresAt IS NULL OR lt.expiresAt > :now)")
    Integer getTotalActivePointsByPatient(@Param("patientId") UUID patientId, @Param("now") LocalDate now);

    @Query("SELECT lt FROM LoyaltyTransaction lt WHERE lt.expiresAt <= :date " +
            "AND lt.transactionType = 'earn'")
    List<LoyaltyTransaction> findExpiringTransactions(@Param("date") LocalDate date);
}
