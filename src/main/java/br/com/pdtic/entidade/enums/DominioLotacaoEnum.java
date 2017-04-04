package br.com.pdtic.entidade.enums;


public enum DominioLotacaoEnum {
	
    DCOI("1", "DCOI"), 
    DEHS("2", "DEHS"),
    DABS("3", "DABS"),
    DGTI("4", "DGTI"),
    PRESIDENCIA("5", "Presidência");
    
    private String id;
    private String descricao;

    private DominioLotacaoEnum(String id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }
}
