package com.cmc.meeron.file.adapter.out;

import com.cmc.meeron.file.domain.AgendaFile;
import org.springframework.data.jpa.repository.JpaRepository;

interface AgendaFileJpaRepository extends JpaRepository<AgendaFile, Long> {
}
