package com.campuscrave.api.repository;

import com.campuscrave.api.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByStudentId(Long studentId);

    /**
     * Take money out in one statement, and only if it is there.
     *
     * <p>Returns the rows changed: 0 means the balance was too low. The check and the
     * subtraction happen together in the database, so two orders landing at once cannot
     * both read the same balance and each write back their own answer.
     */
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Wallet w set w.balanceRupees = w.balanceRupees - :rupees"
            + " where w.student.id = :studentId and w.balanceRupees >= :rupees")
    int debit(@Param("studentId") Long studentId, @Param("rupees") int rupees);

    /** Put money in, in one statement. */
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Wallet w set w.balanceRupees = w.balanceRupees + :rupees where w.student.id = :studentId")
    int credit(@Param("studentId") Long studentId, @Param("rupees") int rupees);
}
