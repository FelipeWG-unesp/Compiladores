import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {
    public List<Token> analisar(String codigo) {
        List<Token> tokens = new ArrayList<>();
        
        // Definição das ERs baseadas no seu alfabeto
        // Ordem importa: Real deve vir antes de Inteiro para não capturar errado
        String regex = "(?<nReal>[0-9]+\\.[0-9]+)|(?<nInt>[0-9]+)|(?<opSoma>\\+)|(?<opSub>-)|(?<opMult>\\*)|(?<opDiv>/)|(?<aP>\\()|(?<fP>\\))|(?<erro>[^\\s])";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(codigo);

        while (matcher.find()) {
            if (matcher.group("nReal") != null) tokens.add(new Token(matcher.group("nReal"), "nReal", "Número Real"));
            else if (matcher.group("nInt") != null) tokens.add(new Token(matcher.group("nInt"), "nInt", "Número Inteiro"));
            else if (matcher.group("opSoma") != null) tokens.add(new Token("+", "opSoma", "Operador Soma"));
            else if (matcher.group("opSub") != null) tokens.add(new Token("-", "opSub", "Operador Subtração"));
            else if (matcher.group("opMult") != null) tokens.add(new Token("*", "opMult", "Operador Multiplicação"));
            else if (matcher.group("opDiv") != null) tokens.add(new Token("/", "opDiv", "Operador Divisão"));
            else if (matcher.group("aP") != null) tokens.add(new Token("(", "aP", "Parêntese Aberto"));
            else if (matcher.group("fP") != null) tokens.add(new Token(")", "fP", "Parêntese Fechado"));
            else if (matcher.group("erro") != null) tokens.add(new Token(matcher.group("erro"), "ERRO", "ERRO: Símbolo fora do alfabeto"));
        }
        return tokens;
    }
}
