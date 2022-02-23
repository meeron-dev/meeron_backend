package com.cmc.meeron.workspace.domain.repository;

import com.cmc.meeron.workspace.domain.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

interface WorkspaceJpaRepository extends JpaRepository<Workspace, Long> {
}
