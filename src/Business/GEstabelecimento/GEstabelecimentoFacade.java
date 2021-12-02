package Business.GEstabelecimento;

import Business.GReparacao.GReparacaoFacade;

import java.util.Map;

public class GEstabelecimentoFacade {
    Map<Integer, Funcionario> funcionarios;
    Map<Integer, Tecnico> tecnicos;
    Map<Integer, Cliente> clientes;
    Gestor gestor;

    Map<Integer, Equipamento> equipamentos;

    GReparacaoFacade reparacaoFacade;


    public boolean loginFuncionario(int id, String password) throws FuncionarioNaoExisteException {
        Funcionario fun = this.funcionarios.get(id);
        if(fun != null) {
            return fun.checkPassword(password);
        }else{
            throw new FuncionarioNaoExisteException();
        }
    }

    public boolean checkClient(int nif){
        return this.clientes.containsKey(nif);
    }

    public Cliente registerNewClient(String name, int nif, String email){
        return new Cliente(name,nif,email);
    }

    public void addEquipamentoExpresso(int id, int idEquipamento){
        Funcionario f = this.funcionarios.get(id);
        Equipamento eq = f.registaEntregaExpresso(idEquipamento);
        this.equipamentos.put(idEquipamento,eq);

        this.reparacaoFacade.addToExpressoList(idEquipamento);

    }

    public void addEquipamentoNormal(int id, int idEquipamento, String descricao){
        Funcionario f = this.funcionarios.get(id);
        Equipamento eq = f.registaEntregaNormal(idEquipamento,descricao);
        this.equipamentos.put(idEquipamento,eq);

        this.reparacaoFacade.registarPedidoOrcamento(idEquipamento);
    }

}
