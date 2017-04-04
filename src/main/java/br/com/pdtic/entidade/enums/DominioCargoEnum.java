package br.com.pdtic.entidade.enums;


public enum DominioCargoEnum {
	
    DIRETOR("1", "Diretor"), 
    ASSESSOR("2", "Assessor"),
    ASSISTENTE("3", "Assistente"),
    COORDENADOR_GERAL("4", "Coordenador(a) Geral"),
    COORDENADOR_TECNICO("5", "Coordenador(a) Técnico"),
    CHEFE_SERVICO("6", "Chefe de Serviço");
    
    private String id;
    private String descricao;

    private DominioCargoEnum(String id, String descricao) {
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
