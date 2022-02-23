package com.cmc.meeron.user.domain.repository;

import com.cmc.meeron.user.domain.WorkspaceUser;
import org.springframework.data.jpa.repository.JpaRepository;

interface WorkspaceUserJpaRepository extends JpaRepository<WorkspaceUser, Long> {
}
