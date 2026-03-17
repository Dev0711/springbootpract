package com.project.springpract.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "userdb")
public class User implements Persistable<UUID> {

    @Id
    private UUID id;

    @NotNull
    private String name;

    private String email;

    private String password;
    private String phoneNumber;

    private UserRole role;

    private UserStatus userStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addresses;

    // --- Persistable + UUID generation ---

    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }

    /**
     * Auto-generate UUID when not pre-set (normal createUser flow).
     * When pre-set (internal createUserInternal flow), this is skipped.
     */
    @PrePersist
    void generateIdIfNeeded() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public void addAddress(Address address) {
        addresses.add(address);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
    }
}
