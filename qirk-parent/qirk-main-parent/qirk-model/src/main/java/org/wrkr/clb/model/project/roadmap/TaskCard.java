package org.wrkr.clb.model.project.roadmap;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.wrkr.clb.model.BaseIdEntity;
import org.wrkr.clb.model.BaseVersionedEntityMeta;
import org.wrkr.clb.model.VersionedIdEntity;
import org.wrkr.clb.model.project.Project;
import org.wrkr.clb.model.project.task.Task;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = TaskCardMeta.TABLE_NAME)
public class TaskCard extends BaseIdEntity implements VersionedIdEntity {

    @Column(name = BaseVersionedEntityMeta.recordVersion, nullable = false)
    private Long recordVersion = 1L;

    public static enum Status {
        STOPPED("STOPPED"),
        ACTIVE("ACTIVE"),
        PAUSED("PAUSED"),
        COMPLETED("COMPLETED");

        @SuppressWarnings("unused")
        private final String nameCode;

        Status(final String nameCode) {
            this.nameCode = nameCode;
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    @Transient
    private Long projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "road_id", nullable = false)
    private Road road;
    @Transient
    private Long roadId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, unique = true)
    @JsonProperty(value = "status")
    private Status status = Status.STOPPED;

    @Column(name = "active", nullable = false)
    private boolean active = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_id", nullable = true)
    private TaskCard previous;
    @Transient
    private Long previousId;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "archieved_at", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = true)
    private OffsetDateTime archievedAt;

    // OneToOne is not used because hibernate fetches reverse OneToOnes eagerly
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "previous")
    private List<TaskCard> nextCards;
    @Transient
    private Long nextId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "card")
    private List<Task> tasks = new ArrayList<Task>();

    @Override
    public Long getRecordVersion() {
        return recordVersion;
    }

    @Override
    public void setRecordVersion(Long recordVersion) {
        this.recordVersion = recordVersion;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        this.projectId = project.getId();
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
        this.project = null;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
        this.roadId = (road == null ? null : road.getId());
    }

    public Long getRoadId() {
        return roadId;
    }

    public void setRoadId(Long roadId) {
        this.roadId = roadId;
        this.road = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<TaskCard> getNextCards() {
        return nextCards;
    }

    public void setNextCards(List<TaskCard> nextCards) {
        this.nextCards = nextCards;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TaskCard getPrevious() {
        return previous;
    }

    public void setPrevious(TaskCard previous) {
        this.previous = previous;
        this.previousId = (previous == null ? null : previous.getId());
    }

    public Long getPreviousId() {
        return previousId;
    }

    public void setPreviousId(Long previousId) {
        this.previousId = previousId;
        this.previous = null;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getArchievedAt() {
        return archievedAt;
    }

    public void setArchievedAt(OffsetDateTime archievedAt) {
        this.archievedAt = archievedAt;
    }

    public TaskCard getNext() {
        if (nextCards.isEmpty()) {
            return null;
        }
        return nextCards.get(0);
    }

    public void setNext(TaskCard next) {
        this.nextCards = Arrays.asList(next);
    }

    public Long getNextId() {
        return nextId;
    }

    public void setNextId(Long nextId) {
        this.nextId = nextId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
