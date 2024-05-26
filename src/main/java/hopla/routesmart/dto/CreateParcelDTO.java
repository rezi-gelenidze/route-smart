package hopla.routesmart.dto;

public class CreateParcelDTO {
    private Long identifier;
    private Long fromId;
    private Long toId;

    // Constructors, getters, and setters
    public CreateParcelDTO() {}

    public CreateParcelDTO(Long identifier, Long fromId, Long toId) {
        this.identifier = identifier;
        this.fromId = fromId;
        this.toId = toId;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }
}
