	package plp.enquanto.linguagem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public interface Linguagem {
	final Map<String, Integer> ambiente = new HashMap<>();
	final Map<String, DefFuncao> defs = new HashMap<>();
	final Scanner scanner = new Scanner(System.in);

	interface Bool {
		public boolean getValor();
	}

	interface Comando {
		public void execute();
	}

	interface Expressao {
		public int getValor();
	}

	abstract class ExpBin implements Expressao {
		protected Expressao esq;
		protected Expressao dir;

		public ExpBin(Expressao esq, Expressao dir) {
			this.esq = esq;
			this.dir = dir;
		}
	}

	class Programa {
		private List<Comando> comandos;
		public Programa(List<Comando> comandos) {
			this.comandos = comandos;
		}
		public void execute() {
			for (Comando comando : comandos) {
				comando.execute();
			}
		}
	}

	class Se implements Comando {
		private Bool condicao;
		private Comando entao;
		private Comando senao;
		private Map<Bool, Comando> senaoses;

		public Se(Bool condicao, Comando entao, Map<Bool, Comando> senaoses, Comando senao) {
			this.condicao = condicao;
			this.entao = entao;
			this.senao = senao;
			this.senaoses = senaoses;
		}

		@Override
		public void execute() {
			if (condicao.getValor()) {
				entao.execute();
			} else {
				Iterator<Bool> condicoes = senaoses.keySet().iterator();
				while (condicoes.hasNext()) {
					Bool senaose_condicao = condicoes.next();
					if (senaose_condicao.getValor()) {
						senaoses.get(senaose_condicao).execute();
						return;
					}
				}
				
				senao.execute();
			}
		}
	}

	Skip skip = new Skip();
	class Skip implements Comando {
		@Override
		public void execute() {
		}
	}

	class Escreva implements Comando {
		private Expressao exp;

		public Escreva(Expressao exp) {
			this.exp = exp;
		}

		@Override
		public void execute() {
			System.out.println(exp.getValor());
		}
	}

	class Enquanto implements Comando {
		private Bool condicao;
		private Comando faca;

		public Enquanto(Bool condicao, Comando faca) {
			this.condicao = condicao;
			this.faca = faca;
		}

		@Override
		public void execute() {
			while (condicao.getValor()) {
				faca.execute();
			}
		}
	}
	
	class Escolha implements Comando {
		private Id entrada;
        private Map<Expressao, Comando> comandos;
        private Comando saidaPadrao;

		public Escolha(Id entrada, Map<Expressao, Comando> comandos, Comando saidaPadrao) {
			this.entrada = entrada;
			this.comandos = comandos;
			this.saidaPadrao = saidaPadrao;
		}
		
		@Override
		public void execute() {
			int valor = entrada.getValor();
			for (Expressao exp : comandos.keySet()) {
                if (exp.getValor() == valor) {
                    comandos.get(exp).execute();
                    return;
                }
			}
			saidaPadrao.execute();
		}
		
	}
	
	class Para implements Comando {
		private Id id;
		private Expressao esq;
		private Expressao dir;
		private Comando faca;
		private Inteiro passo;
		
		public Para(Id id, Expressao esq, Expressao dir, Comando faca, Inteiro passo) {
			this.id = id;
			this.esq = esq;
			this.dir = dir;
			this.faca = faca;
			this.passo = passo;
		}

		@Override
		public void execute() {
			for (int i = esq.getValor(); i <= dir.getValor(); i+=(int) passo.getValor()) {
				ambiente.put(id.id, i);
				faca.execute();
			}
		}
	}
	
	class DefFuncao implements Comando {
		private Id nome;
		private List<Id> parametros;
		private Expressao expressao;
		
		public DefFuncao (Id nome, List<Id> parametros, Expressao expressao) {
			this.nome = nome;
			this.parametros = parametros;
			this.expressao = expressao;
		}
		
		public int chamada(List<Expressao> valores) throws Exception {
			if (valores.size() == parametros.size()) {
				for (int i = 0; i < parametros.size(); i++) {
					ambiente.put(parametros.get(i).id, valores.get(i).getValor());
				}
				return expressao.getValor();
			} else {
				throw new Exception("Quantidade de parâmetros errada");
			}
		}
		
		@Override
		public void execute() {
			defs.put(nome.id, this);
		}
	}
	
	class ChamadaFuncao implements Expressao {
		private Id nome;
		private List<Expressao> valores;
		
		public ChamadaFuncao (Id nome, List<Expressao> valores) {
			this.nome = nome;
			this.valores = valores;
		}
		
		@Override
		public int getValor() {
			try {
				return defs.get(nome.id).chamada(valores);
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
	}

	class Exiba implements Comando {
		public Exiba(String texto) {
			this.texto = texto;
		}

		private String texto;

		@Override
		public void execute() {
			System.out.println(texto);
		}
	}

	class Bloco implements Comando {
		private List<Comando> comandos;

		public Bloco(List<Comando> comandos) {
			this.comandos = comandos;
		}

		@Override
		public void execute() {
			for (Comando comando : comandos) {
				comando.execute();
			}
		}
	}

	class Atribuicao implements Comando {
		private String id;
		private Expressao exp;

		public Atribuicao(String id, Expressao exp) {
			this.id = id;
			this.exp = exp;
		}

		@Override
		public void execute() {
			ambiente.put(id, exp.getValor());
		}
	}

	class Inteiro implements Expressao {
		private int valor;

		public Inteiro(int valor) {
			this.valor = valor;
		}

		@Override
		public int getValor() {
			return valor;
		}
	}

	class Id implements Expressao {
		private String id;

		public Id(String id) {
			this.id = id;
		}

		@Override
		public int getValor() {
			final Integer v = ambiente.get(id);
			final int valor;
			if (v != null)
				valor = v;
			else
				valor = 0;

			return valor;
		}
	}

	Leia leia = new Leia();
	class Leia implements Expressao {
		@Override
		public int getValor() {
			return scanner.nextInt();
		}
	}

	class ExpSoma extends ExpBin {
		public ExpSoma(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public int getValor() {
			return esq.getValor() + dir.getValor();
		}
	}

	class ExpSub extends ExpBin {
		public ExpSub(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public int getValor() {
			return esq.getValor() - dir.getValor();
		}
	}
	
	class ExpMult extends ExpBin {
		public ExpMult(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public int getValor() {
			return esq.getValor() * dir.getValor();
		}
	}
	
	class ExpDiv extends ExpBin {
		public ExpDiv(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public int getValor() {
			return esq.getValor() / dir.getValor();
		}
	}
	
	class ExpPot extends ExpBin {
		public ExpPot(Expressao esq, Expressao dir) {
			super(esq, dir);
		}	

		@Override
		public int getValor() {
			return (int)Math.pow(esq.getValor(), dir.getValor());
		}
	}

	class Booleano implements Bool {
		private boolean valor;

		public Booleano(boolean valor) {
			this.valor = valor;
		}

		@Override
		public boolean getValor() {
			return valor;
		}
	}

	abstract class ExpRel implements Bool {
		protected Expressao esq;
		protected Expressao dir;

		public ExpRel(Expressao esq, Expressao dir) {
			this.esq = esq;
			this.dir = dir;
		}
	}
	
	public class ExpIgual extends ExpRel {

		public ExpIgual(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public boolean getValor() {
			return esq.getValor() == dir.getValor();
		}

	}
	
	public class ExpDif extends ExpRel {

		public ExpDif(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public boolean getValor() {
			return esq.getValor() != dir.getValor();
		}

	}

	public class ExpMenorIgual extends ExpRel {
		public ExpMenorIgual(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public boolean getValor() {
			return esq.getValor() <= dir.getValor();
		}
	}
	
	public class ExpMaiorIgual extends ExpRel {
		public ExpMaiorIgual(Expressao esq, Expressao dir) {
			super(esq, dir);
		}

		@Override
		public boolean getValor() {
			return esq.getValor() >= dir.getValor();
		}
	}

	public class NaoLogico implements Bool {
		private Bool b;

		public NaoLogico(Bool b) {
			this.b = b;
		}

		@Override
		public boolean getValor() {
			return !b.getValor();
		}
	}

	public class ELogico implements Bool {
		private Bool esq;
		private Bool dir;

		public ELogico(Bool esq, Bool dir) {
			this.esq = esq;
			this.dir = dir;
		}

		@Override
		public boolean getValor() {
			return esq.getValor() & dir.getValor();
		}
	}
	
	public class OuLogico implements Bool {
		private Bool esq;
		private Bool dir;

		public OuLogico(Bool esq, Bool dir) {
			this.esq = esq;
			this.dir = dir;
		}

		@Override
		public boolean getValor() {
			return esq.getValor() | dir.getValor();
		}
	}
	
	public class XorLogico implements Bool {
		private Bool esq;
		private Bool dir;

		public XorLogico(Bool esq, Bool dir) {
			this.esq = esq;
			this.dir = dir;
		}

		@Override
		public boolean getValor() {
			return esq.getValor() ^ dir.getValor();
		}
	}
}
