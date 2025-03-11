package com.groupeisi.School.repository;
import com.groupeisi.School.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long>{
}
