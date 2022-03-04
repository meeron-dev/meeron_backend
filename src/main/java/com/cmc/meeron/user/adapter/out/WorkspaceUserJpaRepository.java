package com.cmc.meeron.user.adapter.out;

import com.cmc.meeron.user.domain.WorkspaceUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface WorkspaceUserJpaRepository extends JpaRepository<WorkspaceUser, Long> {

    List<WorkspaceUser> findByUserId(Long userId);
}
