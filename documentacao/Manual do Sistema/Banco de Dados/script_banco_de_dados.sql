-- Usuário
INSERT INTO `projeto_ensino`.`csp` (`prontuario`, `email`, `nome`, `senha`) VALUES ('PE1111111', 'csp1@email.com', 'Marcos', 'b8db36daef560a8c49c2c12d6a935e0c4122cf10b415e612edd421b6b2002ef170535505c5d19ff30f898a08ee786b1ef7588272588f7cac893c5cf2e6fb5485');
INSERT INTO `projeto_ensino`.`professor` (`prontuario`, `email`, `nome`, `senha`, `status`) VALUES ('PE2222222', 'professor1@email.com', 'Antônio', 'b8db36daef560a8c49c2c12d6a935e0c4122cf10b415e612edd421b6b2002ef170535505c5d19ff30f898a08ee786b1ef7588272588f7cac893c5cf2e6fb5485', '1');
INSERT INTO `projeto_ensino`.`aluno` (`prontuario`, `email`, `nome`, `senha`, `CPF`, `rg`, `orgao_emissor`, `semestre`, `celular`, `banco`, `agencia`, `conta_corrente`, `turno`, `idCurso`) VALUES ('PE3333333', 'aluno1@email.con', 'João', 'b8db36daef560a8c49c2c12d6a935e0c4122cf10b415e612edd421b6b2002ef170535505c5d19ff30f898a08ee786b1ef7588272588f7cac893c5cf2e6fb5485', '123.456.789-0', '12.345.678-9', 'SP', '1', '18987654321', 'Santander', '123', '456', '1', '1');

-- Curso
INSERT INTO `projeto_ensino`.`curso` (`idCurso`, `nome_curso`) VALUES ('1', 'Ciência da Computação');

-- Disciplina
INSERT INTO `projeto_ensino`.`disciplina` (`iddisciplina`, `nome`, `idCurso`) VALUES ('1', 'Banco de Dados I', '1');
INSERT INTO `projeto_ensino`.`disciplina` (`iddisciplina`, `nome`, `idCurso`) VALUES ('2', 'Inteligência Artificial', '1');
INSERT INTO `projeto_ensino`.`disciplina` (`iddisciplina`, `nome`, `idCurso`) VALUES ('3', 'Ferramentas de Programação I', '1');

-- Edital (Sem PDF)
INSERT INTO `projeto_ensino`.`edital` (`idedital`, `dataabertura`, `datafechamento`, `resumoedital`, `tipoedital`, `status`) VALUES ('1', '2024-11-30', '2024-12-10', 'Resumo 1', 'novo_projeto', 'Aprovado');
INSERT INTO `projeto_ensino`.`edital` (`idedital`, `dataabertura`, `datafechamento`, `resumoedital`, `tipoedital`, `status`) VALUES ('2', '2024-11-30', '2024-12-10', 'Resumo 2', 'novo_projeto', 'Aprovado');
INSERT INTO `projeto_ensino`.`edital` (`idedital`, `dataabertura`, `datafechamento`, `resumoedital`, `tipoedital`, `status`) VALUES ('3', '2024-10-01', '2024-11-06', 'Resumo 3', 'novo_projeto', 'Inativo');

-- Projeto (Sem PDF)
INSERT INTO `projeto_ensino`.`projeto` (`idProjeto`, `titulo`, `resumo`, `duracaoSemestre`, `numBolsista`, `cargaSemanal`, `perfilBolsista`, `atividadesPrevistas`, `dataInicio`, `dataTermino`, `status`, `prontuario`, `idedital`) VALUES ('1', 'Titulo 1', 'a', '1', '1', '40', 'a', 'a', '2024-11-30', '2024-12-11', 'Aprovado', 'PE2222222', '1');

