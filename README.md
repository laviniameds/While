While
=====

Linguagem "Enquanto" usada como exemplo na disciplina de Paradigmas de Linguagens de Programação

> Implementação da linguagem em Scala (~250 linhas de código): [whilelang](http://github.com/lrlucena/whilelang)

## Projeto de Paradigmas de Linguagens de Programação

### Altere a linguagem para incluir:
1. A expressão de divisão de inteiros (`/`) **OK**
2. A expressão de exponenciação de inteiros (`^`) **OK**
3. A expressão `ou` entre booleanos **OK**
4. A expressão `xor` entre booleanos **OK**
5. A relação maior ou igual (`>=`) **OK**
6. A relação diferente (`<>`) **OK**
5. O comando `para ID de expressao ate expressao [passo Int] faca comando`. **OK** Exemplo: 
<pre>
    para i de 1 ate 5 passo 2 faca
      escreva i
</pre>
8. A alteração do comando `se .. entao .. senao ...` para incluir a cláusula (opcional) `senaose`. Cada `se` pode 
ter zero ou mais clausulas `senaose`. **OK** Exemplo:
<pre>
    se nota >= 6 entao
      exiba "Aprovado"
    senaose nota >= 3 entao
      exiba "Recuperação"
    senao
      exiba "Reprovado"
</pre>

9. O comando `escolha` (switch). **OK**
<pre>
    x = leia
    escolha x
      caso 1 : exiba "um"
      caso 2 : exiba "dois"
      outro  : exiba "outro numero"
 </pre>

10. A definição e aplicação de funções **OK**
<pre>
  soma(a,b) = a + b ;
  
  soma(2,3)
</pre>
