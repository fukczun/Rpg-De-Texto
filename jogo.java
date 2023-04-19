package jandilson2;

import java.text.Normalizer;
import java.util.Random;
import java.util.Scanner;


public class jogo {

	public static String remover_acentos(String str) {
		return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	public static boolean guardar_item(Personagem crianca, String item, char tipo) {
		if (tipo == 'e') {
			crianca.tesouros_adquiridos[crianca.local_tesouro] = item;
			crianca.local_tesouro++;
			return true;
		} else {
			if (crianca.local_inventario >= crianca.inventario.length) {
				System.out.println("* Sua mochila está cheia, descarte algum item...");
				try {
					Thread.sleep(600);
				} catch (Exception erro) {
				}
				return false;
			} else {
				crianca.inventario[crianca.local_inventario] = item;
				crianca.local_inventario++;
				System.out.println("* Você pega " + item);
				try {
					Thread.sleep(400);
				} catch (Exception erro) {
				}
				return true;
			}
		}
	}

	public static boolean remover_item_inventario(Personagem crianca, String item) {
		for (int i = 0; i < crianca.local_inventario; i++) {
			if (crianca.inventario[i].equals(item)) {
				for (int j = i; j < crianca.inventario.length - 1; j++) {
					crianca.inventario[j] = crianca.inventario[j + 1];
				}
				crianca.inventario[crianca.inventario.length - 1] = null;
				crianca.local_inventario--;
				return true;
			}
		}
		return false;
	}

	public static boolean remover_item_mercado(Mercado shop, String item) {
		for (int i = 0; i < shop.quantidade_produtos; i++) {
			if (shop.produtos[i].equals(item)) {
				for (int j = i; j < shop.produtos.length - 1; j++) {
					shop.produtos[j] = shop.produtos[j + 1];
					shop.precos[j] = shop.precos[j + 1];
				}
				shop.produtos[shop.produtos.length - 1] = null;
				shop.precos[shop.precos.length - 1] = -1;
				shop.quantidade_produtos--;
				return true;
			}
		}
		return false;
	}

	public static void substituir_item(Personagem crianca, String valor_antigo, String novo_valor) {
		for (int i = 0; i < crianca.local_inventario; i++) {
			if (crianca.inventario[i].equals(valor_antigo)) {
				crianca.inventario[i] = novo_valor;
				System.out.println("* Você troca " + valor_antigo + " por " + novo_valor);
				try {
					Thread.sleep(450);
				} catch (Exception erro) {
				}
			}
		}
	}

	public static String verificar_instrucao(String instrucao_cliente, String[] instrucoes_validas) {
		instrucao_cliente = remover_acentos(instrucao_cliente.toUpperCase());
		String[] instrucao_partes = instrucao_cliente.split(" ");

		for (String instrucao_valida : instrucoes_validas) {
			for (String possivel_instrucao : instrucao_partes) {
				if (possivel_instrucao.equals(instrucao_valida)) {
					return possivel_instrucao;
				}
			}
		}

		return "";
	}

	public static void tesouro_pesca(Personagem crianca) {
		Random random = new Random();
		String[] tesouros = { "POCAO-DE-VITALIDADE", "ESPADA-ENCANTADA", "ARMADURA-PESADA" };

		String tesouro_ganho = tesouros[random.nextInt(tesouros.length)];

		while (tem_elemento(tesouro_ganho, crianca.tesouros_adquiridos)) {
			tesouro_ganho = tesouros[random.nextInt(tesouros.length)];
		}
		System.out.println("Você conseguiu pegar... Minha nossa! Isso não é um peixe!");
		try {
			Thread.sleep(750);
		} catch (Exception erro) {
		}

		if (tesouro_ganho == "ARMADURA-PESADA") {
			System.out.println("* Você acha " + tesouro_ganho);

			if (tem_elemento("ARMADURA-DRACONICA", crianca.inventario)) {
				System.out.println("Mas ela é ridícula perto de sua armadura atual...");
			} else {
				crianca.defesa = 25;
				System.out.println("Sua defesa foi aprimorada!");
			}
		} else if (tesouro_ganho == "POCAO-DE-VITALIDADE") {
			System.out.println("* Você acha " + tesouro_ganho);

			crianca.vida += 50;
			System.out.println("Você bebe ela ali mesmo e sente sua vida melhorar!");
		} else {
			if (tem_elemento("ESPADA-ENFERRUJADA", crianca.inventario)) {
				System.out.println("* Você acha " + tesouro_ganho);

				substituir_item(crianca, "ESPADA-ENFERRUJADA", "ESPADA-ENCANTADA");
			} else if (tem_elemento("ESPADA", crianca.inventario)) {
				System.out.println("* Você acha " + tesouro_ganho);

				substituir_item(crianca, "ESPADA", "ESPADA-ENCANTADA");
			} else {
				guardar_item(crianca, tesouro_ganho, 'n');
			}
			System.out.println("Seu ataque foi aprimorado!");
		}
		guardar_item(crianca, tesouro_ganho, 'e');
	}

	public static void pescar(Personagem crianca) {
		if (crianca.local_inventario >= crianca.inventario.length) {
			System.out.println("* Sua mochila está cheia, descarte algum item...");
		} else {
			Random random = new Random();
			int sorte_pesca = random.nextInt(101);

			if (crianca.local_tesouro == 3) {
				sorte_pesca -= 3;
			}

			if (sorte_pesca <= 100 && sorte_pesca >= 98) {
				tesouro_pesca(crianca);
			} else if (sorte_pesca < 98 && sorte_pesca >= 16) {
				guardar_item(crianca, "PEIXE", 'n');
			} else {
				System.out.println("Que azar! Todos os peixinhos fugiram quando você jogou a vara...");
			}
		}
	}

	public static int treinar(Personagem crianca) {
		Random random = new Random();
		int concentracao = random.nextInt(101), especial = 1;

		if (tem_elemento("ESPADA-ENCANTADA", crianca.inventario)) {
			especial = 2;
		}

		if (concentracao <= 100 && concentracao >= 90) {
			System.out.println("Suas veias saltam com tanta adrenalina!");
			return 3 * especial;
		} else if (concentracao < 90 && concentracao >= 16) {
			System.out.println("Foi um bom treino, mas posso melhorar!");
			return 1 * especial;
		}
		System.out.println("Não estava tão concentrado dessa vez...");
		return 0;
	}

	public static void moeda_floresta(Personagem crianca) {
		Random random = new Random();
		int sorte = random.nextInt(101);
		if (sorte <= 100 && sorte >= 95) {
			System.out.println("Você encontrou uma moeda enquanto saia pela floresta!");
			crianca.moedas++;
			try {
				Thread.sleep(550);
			} catch (Exception erro) {
			}
		}
	}

	public static void menu_mercado(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		System.out.println("O que você deseja comprar ou fazer? || Saldo: " + crianca.moedas + " moedas\n");
		for (int i = 0; i < shop.quantidade_produtos; i++) {
			System.out.println("[" + (i + 1) + "] " + shop.produtos[i] + " --> " + shop.precos[i] + " moedas");
		}
		System.out.println("[" + (shop.quantidade_produtos + 1) + "] VENDER PEIXE --> 2 MOEDAS");

		int sair = shop.quantidade_produtos + 2;
		System.out.println("[" + sair + "] SAIR");
		int produto_escolhido = scan.nextInt();

		if (produto_escolhido > 0 && produto_escolhido <= shop.quantidade_produtos) {
			if (crianca.local_inventario >= crianca.inventario.length) {
				System.out.println("* Sua mochila está cheia, descarte algum item...");
			} else {
				if (shop.precos[produto_escolhido - 1] <= crianca.moedas) {
					crianca.moedas -= shop.precos[produto_escolhido - 1];

					if (shop.produtos[produto_escolhido - 1] == "MEDALHAO-MISTERIOSO") {
						guardar_item(crianca, "MEDALHAO-MISTERIOSO", 'n');
					} else {
						crianca.defesa = 50;
						System.out.println("* Você pega ARMADURA-DRACONICA");
					}

					remover_item_mercado(shop, shop.produtos[produto_escolhido - 1]);
				} else {
					System.out.println("Dinheiro insuficiente...\n");
				}
			}
		} else if (produto_escolhido == (shop.quantidade_produtos + 1)) {
			if (tem_elemento("PEIXE", crianca.inventario)) {
				remover_item_inventario(crianca, "PEIXE");
				crianca.moedas += 2;
				System.out.println("Você vende o peixe!");
			} else {
				System.out.println("Você não tem peixes para vender!");
			}
		} else if (produto_escolhido == (sair)) {
			System.out.println("Você pode voltar para a FLORESTA, falar com o PAI ou com o FERREIRO...");
			vendedor(crianca, shop);
		} else {
			System.out.println("Valor inexistente...\n");
		}
		while (produto_escolhido != sair) {
			menu_mercado(crianca, shop);
		}
	}

	public static boolean tem_elemento(String string, String[] lista) {
		for (String elemento : lista) {
			if (elemento == null) {
				continue;
			} else if (elemento.equals(string)) {
				return true;
			}
		}
		return false;
	}

	public static void falas(String aldeao, Personagem crianca, int poder_ogro) {
		Random random = new Random();

		if (aldeao.equals("PAI")) {
			String[] falas = { "Cuidado com a floresta meu filho falam que ela está perigosa...",
					"Sua mãe teria orgulho de te ver assim", "Lembre de escovar esses seus dentes!",
					"Eu ouvi histórias sobre uma fera grandiosa à solta", "É sempre bom te ter aqui!" };
			int indice_fala = random.nextInt(falas.length);

			System.out.println(falas[indice_fala]);
		} else {
			String[] falas = { "Os ogros gostam muito de escuro, ao menos foi o que me disseram",
					"Sabia que a Katana é uma forma de arte no Japão?",
					"Esvazie sua mente e então o mundo se preencherá", "Nunca se esqueça de escovar os dentes...", "" };
			int indice_fala = random.nextInt(falas.length);

			if (indice_fala == 4) {
				int margem_de_erro = random.nextInt(10);
				int poder_ogro_aproximado = poder_ogro * (1 + (margem_de_erro / 100));
				if ((crianca.forca + crianca.vida + crianca.defesa) > poder_ogro_aproximado) {
					System.out.println("Você está ficando forte, jovemzinho!");
				} else {
					System.out.println("Você ainda está muito fraco...");
				}
			} else {
				System.out.println(falas[indice_fala]);
			}
		}

	}

	public static double medalhao() {
		Random random = new Random();
		int sorte = random.nextInt(2);

		System.out.println("Você usa o medalhão...");
		try {
			Thread.sleep(750);
		} catch (Exception erro) {
		}
		if (sorte == 1) {
			System.out.println("De dentro dele sai luz e sua vista se ilumina!");
			return 1.25;
		} else {
			System.out.println("De dentro dele sai trevas e sua vista escurece!");
			return 0.75;
		}
	}

	public static boolean batalha_final(int sorte, int chance) {
		Random random = new Random();
		chance = 100 / chance;
		int resto = random.nextInt(chance);

		if (sorte % chance == resto) {
			return true;
		}
		return false;
	}

	public static void conquistas(Personagem crianca) {
		try {
			Thread.sleep(2500);
		} catch (Exception erro) {
		}
		if (crianca.local_tesouro == 0) {
			System.out.println("\nVocê não conseguiu nenhuma das 3 conquistas possíveis no jogo...");
		}
		else {
			System.out.print("\nVocê conseguiu " + crianca.local_tesouro + " das 3 conquistas possíveis no jogo!");
			if (crianca.local_tesouro == 3) {
				System.out.println(" Parabéns!\n");
			}
			else {
				System.out.println("\n");
			}
			
			for (String conquista: crianca.tesouros_adquiridos) {
				if (conquista ==  null) {
					continue;
				}
				System.out.println("- " + conquista);
			}
			try {
				Thread.sleep(3000);
			} catch (Exception erro) {
			}
			
			System.out.print("\n Fim!");
		}
	}
	
	public static void inicio(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		String[] instrucoes_validas = { "ARVORES" };
		instrucao = verificar_instrucao(instrucao, instrucoes_validas);

		if (instrucao.equals("ARVORES")) {
			System.out.println("Você foi até a árvore e avistou 3 lugares, um pouco distantes... ");
			try {
				Thread.sleep(2000);
			} catch (Exception erro) {
			}

			System.out.println("Uma CAVERNA, um CAMPO raso com algumas armas, " + "algumas CASAS " + "e um LAGO");
			crianca.conhecer_floresta = true;
			floresta(crianca, shop, true);
		} else {
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				inicio(crianca, shop);
			}
		}
	}

	public static void floresta(Personagem crianca, Mercado shop, boolean sem_erro) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		if (crianca.conhecer_vila) {
			String[] instrucoes_validas = { "CAVERNA", "CAMPO", "VILA", "LAGO" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		} else {
			String[] intrucoes_validas = { "CAVERNA", "CAMPO", "CASAS", "LAGO" };
			instrucao = verificar_instrucao(instrucao, intrucoes_validas);
		}

		switch (instrucao) {
		case "CAVERNA":
			if (crianca.conhecer_floresta && sem_erro) {
				moeda_floresta(crianca);
			}

			System.out.println("Você caminha por muito tempo e chega"
					+ " em uma caverna escura. Você pode entrar na CAVERNA ou voltar para a FLORESTA");
			caverna(crianca, shop);
			break;

		case "CAMPO":
			if (crianca.conhecer_floresta && sem_erro) {
				moeda_floresta(crianca);
			}

			if (tem_elemento("ESPADA-ENFERRUJADA", crianca.inventario) || tem_elemento("ESPADA", crianca.inventario)
					|| crianca.viu_espada) {
				System.out.println(
						"Você vai ao campo de treinamento e avista o SACO de pancadas, você também pode voltar para a FLORESTA");
				campo(crianca, shop);
			} else {
				System.out.println("Você chega em um campo de treinamento");
				try {
					Thread.sleep(750);
				} catch (Exception erro) {
				}

				System.out.println(
						"Você, sai da FLORESTA, e encontra algumas ferramentas dentre elas uma ESPADA enfurrejada e "
								+ "mais ao longe você encontra um SACO de pancadas");
				campo(crianca, shop);
			}
			break;

		case "VILA":
			if (crianca.conhecer_floresta && sem_erro) {
				moeda_floresta(crianca);
			}

			System.out.println("Você chega na vila e avista seu PAI, o VENDEDOR e o FERREIRO...");
			vila(crianca, shop);

			break;

		case "CASAS":
			if (crianca.conhecer_floresta && sem_erro) {
				moeda_floresta(crianca);
			}

			System.out.println(
					"Você chega em uma vila calma e aparentemente" + " pouco povoada, você vê um HOMEM se aproximando");
			vila(crianca, shop);
			break;

		case "LAGO":
			if (crianca.conhecer_floresta && sem_erro) {
				moeda_floresta(crianca);
			}

			System.out.println("Você chega num pequeno lago e vê alguns peixes pulando");
			try {
				Thread.sleep(1000);
			} catch (Exception erro) {
			}

			if (tem_elemento("VARA", crianca.inventario)) {
				System.out.println("E você tem uma VARA!");
				lago(crianca, shop);
			} else {
				System.out.println("Uma pena não ter como pescá-los. Você pode voltar para a FLORESTA...");
				lago(crianca, shop);
			}
			break;

		default:
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				floresta(crianca, shop, false);
			}
			break;
		}
	}

	public static void caverna(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		String[] instrucoes_validas = { "CAVERNA", "FLORESTA" };
		instrucao = verificar_instrucao(instrucao, instrucoes_validas);

		switch (instrucao) {
		case "CAVERNA":
			System.out.println("Você encontra uma fera indomável, um ogro"
					+ " muito maior do que qualquer criatura que você já" + " tenha visto e tem sua atenção em você. ");
			try {
				Thread.sleep(3100);
			} catch (Exception erro) {
			}
			if (tem_elemento("ESPADA", crianca.inventario) || tem_elemento("ESPADA-ENFERRUJADA", crianca.inventario)
					|| tem_elemento("ESPADA-ENCANTADA", crianca.inventario)) {
				System.out.println("Você só pode atacar" + " o OGRO");
				caverna2(crianca, shop);
			} else {
				System.out
						.println("Você quer atacar mas sabe que" + " não conseguiria... Melhor fugir para a FLORESTA");
				caverna2(crianca, shop);
			}
			break;

		case "FLORESTA":
			System.out
					.println("Boa opção, você foge da caverna e se sente mais " + " seguro, mesmo sem saber o porquê.");
			try {
				Thread.sleep(2000);
			} catch (Exception erro) {
			}
			if (crianca.conhecer_vila) {
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");
			} else {
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, as CASAS ou o LAGO");
			}
			floresta(crianca, shop, true);
			break;

		default:
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				caverna(crianca, shop);
			}
			break;
		}
	}

	public static void caverna2(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		int poder_ogro = 150;

		if (tem_elemento("ESPADA", crianca.inventario) || tem_elemento("ESPADA-ENFERRUJADA", crianca.inventario)
				|| tem_elemento("ESPADA-ENCANTADA", crianca.inventario)) {
			String[] instrucoes_validas = { "OGRO" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		} else {
			String[] instrucoes_validas = { "FLORESTA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		}

		double bonus = 1;
		if (tem_elemento("MEDALHAO-MISTERIOSO", crianca.inventario)) {
			bonus = medalhao();
		}

		double poder_crianca = (crianca.forca + crianca.defesa + crianca.vida) * bonus;

		switch (instrucao) {
		case "OGRO":
			if (poder_crianca < poder_ogro / 2) {
				System.out.println("Você não tem tempo de reagir e morre para o ogro...");
				try {
					Thread.sleep(3200);
				} catch (Exception erro) {
				}
				System.out.print("Fim!");
			} else if (poder_crianca < poder_ogro && poder_crianca > poder_ogro / 2) {
				System.out.println("Vocês duelam por um bom tempo, mesmo que de forma medíocre...");
				try {
					Thread.sleep(2800);
				} catch (Exception erro) {
				}
				System.out.print("Escolha qualquer número: ");
				int sorte = scan.nextInt();
				
				try {
					Thread.sleep(2500);
				} catch (Exception erro) {
				}
				if (batalha_final(sorte, 25)) {
					System.out.println("Foi por MUITO pouco mas você sai vitorioso! Parabéns você ganhou!");
					conquistas(crianca);
				} else {
					System.out.println("Não foi dessa vez... o ogro ganha.");
					try {
						Thread.sleep(3000);
					} catch (Exception erro) {
					}
					System.out.print("Fim!");
				}
			} else if (crianca.forca == poder_ogro) {
				System.out.println("VOCÊS DUELAM INCRIVELMENTE BEM! Só um detalhe resolveria isso...");
				try {
					Thread.sleep(2800);
				} catch (Exception erro) {
				}
				System.out.print("Escolha qualquer número: ");
				int sorte = scan.nextInt();

				if (batalha_final(sorte, 50)) {
					System.out.println("Foi por pouco mas você sai vitorioso! Parabéns você ganhou!");
					conquistas(crianca);
				} else {
					System.out.println("Foi por pouco mas o ogro ganha... Você tem o respeito dele.");
					System.out.print("Fim!");
				}
			} else {
				System.out.println("Vocês travam uma batalha mortal mas você sai vitorioso! Parabéns você ganhou!");
				conquistas(crianca);
			}
			System.exit(0);

			break;

		case "FLORESTA":
			System.out
					.println("Boa opção, você foge da caverna e se sente mais " + " seguro, mesmo sem saber o porquê.");
			try {
				Thread.sleep(2000);
			} catch (Exception erro) {
			}
			if (crianca.conhecer_vila) {
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");
			} else {
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, as CASAS ou o LAGO");
			}
			floresta(crianca, shop, true);
			break;

		default:
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				caverna2(crianca, shop);
			}
			break;
		}
	}

	public static void campo(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		if (tem_elemento("ESPADA", crianca.inventario)
				|| (tem_elemento("ESPADA-ENCANTADA", crianca.inventario) && crianca.viu_espada)) {
			String[] instrucoes_validas = { "SACO", "FLORESTA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);

			if (instrucao.equals("SACO")) {
				System.out.println("Você começa a treinar ...");
				crianca.forca += treinar(crianca);
				System.out.println("Quer continuar usando o" + " SACO de pancadas ou deseja voltar para a FLORESTA? ");
				saco(crianca, shop);
			} else if (instrucao.equals("FLORESTA")) {
				System.out.println("Voltando para a floresta...");
				if (crianca.conhecer_vila) {
					System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");
				} else {
					System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, as CASAS ou o LAGO");
				}
				floresta(crianca, shop, true);
			} else {
				System.out.println("Você não pode fazer isso!");
				while (instrucao == "") {
					campo(crianca, shop);
				}
			}
		} else if (tem_elemento("ESPADA-ENFERRUJADA", crianca.inventario)) {
			String[] instrucoes_validas = { "SACO", "FLORESTA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);

			if (instrucao.equals("SACO")) {
				System.out.println("Você tem uma espada, mas ela não é ideal para o"
						+ " treinamento, você não tem nada a fazer aqui... Melhor" + " voltar para a FLORESTA");
				saco(crianca, shop);
			} else if (instrucao.equals("FLORESTA")) {
				System.out.println("Voltando para a floresta...");
				try {
					Thread.sleep(600);
				} catch (Exception erro) {
				}
				if (crianca.conhecer_vila) {
					System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");
				} else {
					System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, as CASAS ou o LAGO");
				}
				floresta(crianca, shop, true);
			} else {
				System.out.println("Você não pode fazer isso!");
				while (instrucao == "") {
					campo(crianca, shop);
				}
			}
		}

		else if (tem_elemento("ESPADA-ENCANTADA", crianca.inventario) && !crianca.viu_espada) {
			String[] instrucoes_validas = { "ESPADA", "SACO", "FLORESTA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);

			switch (instrucao) {
			case "ESPADA":
				System.out.println("Você vê a espada mas ela não está ao seu nível! Deseja ir ao"
						+ "\nSACO de pancadas ou voltar para a FLORESTA? ");
				crianca.viu_espada = true;
				espada_enferrujada(crianca, shop);
				break;

			case "SACO":
				System.out.println("Você começa a treinar ...");
				crianca.forca += treinar(crianca);
				System.out.println("Quer continuar usando o" + " SACO de pancadas ou deseja voltar para a FLORESTA? ");
				saco(crianca, shop);
				break;

			case "FLORESTA":
				System.out.println("Voltando para a floresta...");
				try {
					Thread.sleep(600);
				} catch (Exception erro) {
				}
				if (crianca.conhecer_vila) {
					System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");
				} else {
					System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, as CASAS ou o LAGO");
				}
				floresta(crianca, shop, true);
				break;

			default:
				System.out.println("Você não pode fazer isso!");
				while (instrucao == "") {
					campo(crianca, shop);
				}
				break;
			}
		} else {
			String[] instrucoes_validas = { "ESPADA", "SACO", "FLORESTA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);

			switch (instrucao) {
			case "ESPADA":
				guardar_item(crianca, "ESPADA-ENFERRUJADA", 'n');

				System.out.println("Deseja ir ao SACO de pancadas ou voltar para a FLORESTA?");
				espada_enferrujada(crianca, shop);
				break;

			case "SACO":
				System.out.println("Você vê alguns equipamentos de luta mas vê que só conseguria usá-los"
						+ " se tivesse algum armamento, você ainda vê a ESPADA enferrujada...");
				saco(crianca, shop);
				break;

			case "FLORESTA":
				System.out.println("Voltando para a floresta...");
				try {
					Thread.sleep(600);
				} catch (Exception erro) {
				}
				if (crianca.conhecer_vila) {
					System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");
				} else {
					System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, as CASAS ou o LAGO");
				}
				floresta(crianca, shop, true);
				break;

			default:
				System.out.println("Você não pode fazer isso!");
				while (instrucao == "") {
					campo(crianca, shop);
				}
				break;
			}
		}
	}

	public static void saco(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		if (tem_elemento("ESPADA", crianca.inventario) || tem_elemento("ESPADA-ENCANTADA", crianca.inventario)) {
			String[] instrucoes_validas = { "SACO", "FLORESTA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		} else if (tem_elemento("ESPADA-ENFERRUJADA", crianca.inventario)) {
			String[] instrucoes_validas = { "FLORESTA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		} else {
			String[] instrucoes_validas = { "ESPADA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		}

		switch (instrucao) {
		case "SACO":
			if (tem_elemento("ESPADA", crianca.inventario) || tem_elemento("ESPADA-ENCANTADA", crianca.inventario)) {
				System.out.println("Você treina mais...");
				crianca.forca += treinar(crianca);
				System.out.println("Quer continuar usando o" + " SACO de pancadas ou deseja voltar para a FLORESTA? ");
				saco(crianca, shop);
			}
			break;

		case "FLORESTA":
			System.out.println("Voltando para a floresta...");
			try {
				Thread.sleep(600);
			} catch (Exception erro) {
			}
			if (crianca.conhecer_vila) {
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");
			} else {
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, as CASAS ou o LAGO");
			}
			floresta(crianca, shop, true);
			break;

		case "ESPADA":
			guardar_item(crianca, "ESPADA-ENFERRUJADA", 'n');

			System.out.println("Deseja ir ao SACO de pancadas ou voltar para a FLORESTA?");
			espada_enferrujada(crianca, shop);
			break;

		default:
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				saco(crianca, shop);
			}
			break;
		}
	}

	public static void espada_enferrujada(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		String[] instrucoes_validas = { "SACO", "FLORESTA" };
		instrucao = verificar_instrucao(instrucao, instrucoes_validas);

		switch (instrucao) {
		case "SACO":
			if (tem_elemento("ESPADA-ENCANTADA", crianca.inventario)) {
				System.out.println("Você começa a treinar ...");
				crianca.forca += treinar(crianca);
				System.out.println("Quer continuar usando o" + " SACO de pancadas ou deseja voltar para a FLORESTA? ");
				saco(crianca, shop);
			} else {
				System.out.println("Você tem uma espada, mas ela não é ideal para o"
						+ " treinamento, você não tem nada a fazer aqui... Melhor" + " voltar para a FLORESTA");
			}
			saco(crianca, shop);
			break;

		case "FLORESTA":
			System.out.println("Voltando para a floresta...");
			try {
				Thread.sleep(600);
			} catch (Exception erro) {
			}
			if (crianca.conhecer_vila) {
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");
			} else {
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, as CASAS ou o LAGO");
			}
			floresta(crianca, shop, true);

			break;

		default:
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				espada_enferrujada(crianca, shop);
			}
			break;
		}
	}

	public static void vila(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();

		if (crianca.conhecer_vila) {
			String[] instrucoes_validas = { "PAI", "VENDEDOR", "FERREIRO" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);

			switch (instrucao) {
			case "PAI":
				falas("PAI", crianca, 150);
				try {
					Thread.sleep(1500);
				} catch (Exception erro) {
				}
				System.out.println("Você pode voltar para a FLORESTA, falar com o VENDEDOR ou com o FERREIRO...");
				homem(crianca, shop);
				break;

			case "VENDEDOR":
				System.out.println("Bem-vindo ao mercado jovem guerreiro!");
				try {
					Thread.sleep(600);
				} catch (Exception erro) {
				}
				menu_mercado(crianca, shop);
				break;

			case "FERREIRO":
				if (crianca.conhecer_ferreiro) {
					falas("FERREIRO", crianca, 150);
					try {
						Thread.sleep(1500);
					} catch (Exception erro) {
					}
					System.out.println("Você pode voltar para a FLORESTA, falar com o VENDEDOR ou com o seu PAI...");
					ferreiro(crianca, shop);
				} else {
					if (tem_elemento("ESPADA-ENFERRUJADA", crianca.inventario)) {
						System.out.println("Vejo que você tem uma ESPADA enferrujada, pequeno guerreiro...");
						ferreiro(crianca, shop);
					} else {
						System.out.println("Vejo que você não tem uma ESPADA, pequeno guerreiro...");
						ferreiro(crianca, shop);
					}
				}
				break;

			default:
				System.out.println("Você não pode fazer isso!");
				while (instrucao == "") {
					vila(crianca, shop);
				}
				break;
			}

		}

		else {
			String[] instrucoes_validas = { "HOMEM" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);

			if (instrucao.equals("HOMEM")) {
				System.out.println("Meu filho! Você finalmente chegou!"
						+ " Você sabe que é perigoso andar só, por agora! Onde você estava?");
				try {
					Thread.sleep(5000);
				} catch (Exception erro) {
				}

				System.out.println("(Você fala que só andou pela floresta)");
				try {
					Thread.sleep(2000);
				} catch (Exception erro) {
				}
				System.out.println(
						"Entendi... Não saia mais sem me avisar! " + "Acho que o VENDEDOR queria falar com você...");
				homem(crianca, shop);
			} else {
				System.out.println("Você não pode fazer isso!");
				while (instrucao == "") {
					vila(crianca, shop);
				}
			}
		}
	}

	public static void homem(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();

		if (crianca.conhecer_vila) {
			String[] instrucoes_validas = { "FLORESTA", "VENDEDOR", "FERREIRO" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		} else {
			String[] instrucoes_validas = { "VENDEDOR" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		}

		switch (instrucao) {
		case "FLORESTA":
			System.out.println("Voltando para a floresta...");
			try {
				Thread.sleep(600);
			} catch (Exception erro) {
			}
			System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");

			floresta(crianca, shop, true);
			break;

		case "VENDEDOR":
			if (crianca.conhecer_vila) {
				System.out.println("Bem-vindo ao mercado jovem guerreiro!");
				try {
					Thread.sleep(600);
				} catch (Exception erro) {
				}
				menu_mercado(crianca, shop);
			} else {
				System.out.println("Tudo bom campeão?! Te procurei muito para te entegrar um presentinho...");
				try {
					Thread.sleep(2000);
				} catch (Exception erro) {
				}
				System.out.println("Agora que você chegou, pode pegar essa minha VARA!");
				vendedor(crianca, shop);
			}
			break;

		case "FERREIRO":
			if (crianca.conhecer_ferreiro) {
				falas("FERREIRO", crianca, 150);
				try {
					Thread.sleep(1500);
				} catch (Exception erro) {
				}
				System.out.println("Você pode voltar para a FLORESTA, falar com o VENDEDOR ou com o seu PAI...");
				ferreiro(crianca, shop);
			} else {
				if (tem_elemento("ESPADA-ENFERRUJADA", crianca.inventario)) {
					System.out.println("Vejo que você tem uma ESPADA enferrujada, pequeno guerreiro...");
					ferreiro(crianca, shop);
				} else {
					System.out.println("Vejo que você não tem uma ESPADA, pequeno guerreiro...");
					ferreiro(crianca, shop);
				}
			}
			break;

		default:
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				homem(crianca, shop);
			}
			break;
		}
	}

	public static void vendedor(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		if (crianca.conhecer_vila) {
			String[] instrucoes_validas = { "FLORESTA", "PAI", "FERREIRO" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		} else {
			String[] instrucoes_validas = { "VARA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		}

		if (crianca.conhecer_vila) {
			switch (instrucao) {
			case "FLORESTA":
				System.out.println("Voltando para a floresta...");
				try {
					Thread.sleep(600);
				} catch (Exception erro) {
				}
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");

				floresta(crianca, shop, true);
				break;

			case "PAI":
				falas("PAI", crianca, 150);
				try {
					Thread.sleep(1500);
				} catch (Exception erro) {
				}
				System.out.println("Você pode voltar para a FLORESTA, falar com o VENDEDOR ou com o FERREIRO...");
				homem(crianca, shop);
				break;

			case "FERREIRO":
				if (crianca.conhecer_ferreiro) {
					falas("FERREIRO", crianca, 150);
					try {
						Thread.sleep(1500);
					} catch (Exception erro) {
					}
					System.out.println("Você pode voltar para a FLORESTA, falar com o VENDEDOR ou com o seu PAI...");
					ferreiro(crianca, shop);
				} else {
					if (tem_elemento("ESPADA-ENFERRUJADA", crianca.inventario)) {
						System.out.println("Vejo que você tem uma ESPADA enferrujada, pequeno guerreiro...");
						ferreiro(crianca, shop);
					} else {
						System.out.println("Vejo que você não tem uma ESPADA, pequeno guerreiro...");
						ferreiro(crianca, shop);
					}
				}
				break;

			default:
				System.out.println("Você não pode fazer isso!");
				while (instrucao == "") {
					vendedor(crianca, shop);
				}
				break;
			}
		} else {
			if (instrucao.equals("VARA")) {
				guardar_item(crianca, "VARA", 'n');
				System.out.println(
						"Ótimo! Agora você pode pescar rsrs e pode mandar os peixes que eu mesmo vou comprá-los");
				try {
					Thread.sleep(3500);
				} catch (Exception erro) {
				}
				System.out.println(
						"Pagarei 2 moedas por cada peixe! Venha aqui mais vezes, está chegando mercadoria nova...");
				try {
					Thread.sleep(2500);
				} catch (Exception erro) {
				}
				System.out.println("(Você vê um FERREIRO caminhando)");
				vara_vendedor(crianca, shop);
			} else {
				System.out.println("Você não pode fazer isso!");
				while (instrucao == "") {
					vendedor(crianca, shop);
				}
			}
		}
	}

	public static void vara_vendedor(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		String[] instrucoes_validas = { "FERREIRO" };
		instrucao = verificar_instrucao(instrucao, instrucoes_validas);

		if (instrucao.equals("FERREIRO")) {
			System.out.println("Olá mocinho, como vai às aventuras do dia? hahaha...");
			try {
				Thread.sleep(1500);
			} catch (Exception erro) {
			}
			if (tem_elemento("ESPADA-ENFERRUJADA", crianca.inventario)) {
				System.out.println("Olha só, vejo que você tem uma ESPADA... Gostaria de vê-la mais de perto...)");
			} else {
				System.out.println("Estava querendo afiar uma ESPADA hoje...");
			}
			ferreiro(crianca, shop);
		} else {
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				vara_vendedor(crianca, shop);
			}
		}
	}

	public static void ferreiro(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();

		if (crianca.conhecer_ferreiro) {
			String[] instrucoes_validas = { "FLORESTA", "VENDEDOR", "PAI" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		} else {
			String[] instrucoes_validas = { "ESPADA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		}

		if (crianca.conhecer_ferreiro) {
			switch (instrucao) {
			case "FLORESTA":
				System.out.println("Voltando para a floresta...");
				try {
					Thread.sleep(600);
				} catch (Exception erro) {
				}
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");

				floresta(crianca, shop, true);
				break;

			case "VENDEDOR":
				System.out.println("Bem-vindo ao mercado jovem guerreiro!");
				try {
					Thread.sleep(600);
				} catch (Exception erro) {
				}
				menu_mercado(crianca, shop);
				break;

			case "PAI":
				falas("PAI", crianca, 150);
				try {
					Thread.sleep(1500);
				} catch (Exception erro) {
				}
				System.out.println("Você pode voltar para a FLORESTA, falar com o VENDEDOR ou com o FERREIRO...");
				homem(crianca, shop);
				break;

			default:
				System.out.println("Você não pode fazer isso!");
				while (instrucao == "") {
					ferreiro(crianca, shop);
				}
				break;
			}
		} else {
			if (tem_elemento("ESPADA-ENFERRUJADA", crianca.inventario)) {
				if (instrucao.equals("ESPADA")) {
					System.out.println("Eu posso usar minha FORJA e melhorar isso, por apenas 15 moedas...");
					espada_ferreiro(crianca, shop);
				} else {
					System.out.println("Você não pode fazer isso!");
					while (instrucao == "") {
						ferreiro(crianca, shop);
					}
				}
			}

			else if (tem_elemento("ESPADA-ENCANTADA", crianca.inventario) && !crianca.conhecer_ferreiro) {
				if (instrucao.equals("ESPADA")) {
					System.out.println(
							"UAU! Isso é muito melhor que uma simples enferrujada! Não tenho nada perto disso...");
					try {
						Thread.sleep(3500);
					} catch (Exception erro) {
					}
					System.out.println(
							"Bem... Quando quiser ouvir um conselho do velho ferreiro venha aqui de novo! E cuidado com os ogros!");
					try {
						Thread.sleep(3000);
					} catch (Exception erro) {
					}
					System.out.println("Você pode voltar para a FLORESTA, falar com o VENDEDOR ou com o seu PAI...");
					crianca.conhecer_ferreiro = true;

					espada_ferreiro(crianca, shop);
				} else {
					System.out.println("Você não pode fazer isso!");
					while (instrucao == "") {
						ferreiro(crianca, shop);
					}
				}
			}

			else {
				if (instrucao.equals("ESPADA")) {
					System.out.println("Tente procurar por algo na FLORESTA ou em outro lugar...");
					espada_ferreiro(crianca, shop);
				} else {
					System.out.println("Você não pode fazer isso!");
					while (instrucao == "") {
						ferreiro(crianca, shop);
					}
				}
			}
		}

	}

	public static void espada_ferreiro(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		if (tem_elemento("ESPADA-ENFERRUJADA", crianca.inventario)) {
			String[] instrucoes_validas = { "FORJA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		} else if (crianca.conhecer_ferreiro) {
			String[] instrucoes_validas = { "FLORESTA", "VENDEDOR", "PAI" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		} else {
			String[] instrucoes_validas = { "FLORESTA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		}

		switch (instrucao) {
		case "FORJA":
			int preco_forja = 15;

			if (crianca.moedas >= preco_forja) {
				System.out.println("Vejo que você tem dinheiro suficiente para a FORJA!");
				try {
					Thread.sleep(450);
				} catch (Exception erro) {
				}
				System.out.println("Mas pode voltar para a FLORESTA se precisar de tempo...");
				forja(crianca, preco_forja, shop);
			} else {
				System.out.println("Você não tem dinheiro suficiente...");
				try {
					Thread.sleep(450);
				} catch (Exception erro) {
				}
				System.out.println("Vá passear na FLORESTA ou em algum outro lugar e venha quando tiver dinheiro");
				forja(crianca, preco_forja, shop);
			}
			break;

		case "FLORESTA":
			if (!crianca.conhecer_vila) {
				crianca.conhecer_vila = true;
			}

			System.out.println("Voltando para a floresta...");
			try {
				Thread.sleep(600);
			} catch (Exception erro) {
			}
			System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");

			floresta(crianca, shop, true);
			break;

		case "PAI":
			falas("PAI", crianca, 150);
			try {
				Thread.sleep(750);
			} catch (Exception erro) {
			}
			System.out.println("Você pode voltar para a FLORESTA, falar com o VENDEDOR ou com o FERREIRO...");
			homem(crianca, shop);
			break;

		case "VENDEDOR":
			System.out.println("Bem-vindo ao mercado jovem guerreiro!");
			try {
				Thread.sleep(450);
			} catch (Exception erro) {
			}
			menu_mercado(crianca, shop);
			break;

		default:
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				espada_ferreiro(crianca, shop);
			}
			break;
		}
	}

	public static void forja(Personagem crianca, int preco_forja, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		if (crianca.moedas >= preco_forja) {
			String[] instrucoes_validas = { "FORJA", "FLORESTA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		} else {
			String[] instrucoes_validas = { "FLORESTA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		}

		crianca.conhecer_vila = true;
		switch (instrucao) {
		case "FORJA":
			substituir_item(crianca, "ESPADA-ENFERRUJADA", "ESPADA");
			System.out.println("Muito bom! Acho que você vai continuar sua aventura por agora");
			try {
				Thread.sleep(1000);
			} catch (Exception erro) {
			}
			System.out.println("Você pode voltar para a FLORESTA, falar com o VENDEDOR ou com o seu PAI...");
			crianca.conhecer_ferreiro = true;

			forja2(crianca, shop);
			break;

		case "FLORESTA":
			System.out.println("Voltando para a floresta...");
			try {
				Thread.sleep(600);
			} catch (Exception erro) {
			}
			System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");

			floresta(crianca, shop, true);
			break;

		default:
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				forja(crianca, preco_forja, shop);
			}
			break;
		}
	}

	public static void forja2(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		String[] instrucoes_validas = { "FLORESTA", "VENDEDOR", "PAI" };
		instrucao = verificar_instrucao(instrucao, instrucoes_validas);

		switch (instrucao) {
		case "FLORESTA":
			System.out.println("Voltando para a floresta...");
			try {
				Thread.sleep(600);
			} catch (Exception erro) {
			}
			System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");

			floresta(crianca, shop, true);
			break;

		case "PAI":
			falas("PAI", crianca, 150);
			try {
				Thread.sleep(750);
			} catch (Exception erro) {
			}
			System.out.println("Você pode voltar para a FLORESTA, falar com o VENDEDOR ou com o FERREIRO...");
			homem(crianca, shop);
			break;

		case "VENDEDOR":
			System.out.println("Bem-vindo ao mercado jovem guerreiro!");
			try {
				Thread.sleep(450);
			} catch (Exception erro) {
			}
			menu_mercado(crianca, shop);
			break;

		default:
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				espada_ferreiro(crianca, shop);
			}
			break;
		}

	}

	public static void lago(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		if (tem_elemento("VARA", crianca.inventario)) {
			String[] instrucoes_validas = { "VARA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		} else {
			String[] instrucoes_validas = { "FLORESTA" };
			instrucao = verificar_instrucao(instrucao, instrucoes_validas);
		}

		switch (instrucao) {
		case "VARA":
			pescar(crianca);
			System.out.println("Você pode tentar jogar a VARA de novo ou voltar para a FLORESTA...");
			vara(crianca, shop);
			break;

		case "FLORESTA":
			System.out.println("Voltando para a floresta...");
			try {
				Thread.sleep(600);
			} catch (Exception erro) {
			}
			if (crianca.conhecer_vila) {
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");
			} else {
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, as CASAS ou o LAGO");
			}
			floresta(crianca, shop, true);
			break;

		default:
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				lago(crianca, shop);
			}
			break;
		}
	}

	public static void vara(Personagem crianca, Mercado shop) {
		Scanner scan = new Scanner(System.in);

		String instrucao = scan.nextLine();
		String[] instrucoes_validas = { "VARA", "FLORESTA" };
		instrucao = verificar_instrucao(instrucao, instrucoes_validas);

		switch (instrucao) {
		case "VARA":
			pescar(crianca);
			System.out.println("Você pode tentar jogar a VARA de novo ou voltar para a FLORESTA...");
			vara(crianca, shop);
			break;

		case "FLORESTA":
			System.out.println("Voltando para a floresta...");
			try {
				Thread.sleep(600);
			} catch (Exception erro) {
			}
			if (crianca.conhecer_vila) {
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, a VILA ou o LAGO");
			} else {
				System.out.println("Você pode ir para a CAVERNA, o CAMPO de treinamento, as CASAS ou o LAGO");
			}
			floresta(crianca, shop, true);
			break;

		default:
			System.out.println("Você não pode fazer isso!");
			while (instrucao == "") {
				vara(crianca, shop);
			}
			break;
		}
	}

	public static void main(String[] args) {
		Personagem crianca = new Personagem();
		Mercado shop = new Mercado();

		System.out.println(
				"Você é uma criança perdida na floresta e tem algumas ÁRVORES ao seu redor, o que deseja fazer? ");
		inicio(crianca, shop);
	}
}
