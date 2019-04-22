package co.dtech.graduates.services;

import co.dtech.graduates.model.Post;
import co.dtech.graduates.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendedAdvertsService {

    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    // Inner class
    public class AdvertContainer implements Comparable<AdvertContainer> {
        Post advert;
        String[] contentWords;
        Set<String> set;
        int score;

        @Override
        public int compareTo(AdvertContainer o) {
            return Integer.compare(score, o.score);
        }
    }

    private AdvertContainer[] advertContainer;
    private Set<String> stopwordsSet;

    RecommendedAdvertsService() {
        //System.err.println("****************************************************************** init");
        stopwordsSet = new HashSet<>();
        Collections.addAll(stopwordsSet, stopwords);
    }

    public static String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across",
            "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone",
            "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any",
            "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear",
            "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at",
            "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been",
            "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better",
            "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant",
            "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes",
            "concerning", "consequently", "consider", "considering", "contain", "containing", "contains",
            "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did",
            "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during",
            "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et",
            "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly",
            "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows",
            "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting",
            "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens",
            "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here",
            "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his",
            "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored",
            "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar",
            "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep",
            "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least",
            "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd",
            "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most",
            "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need",
            "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone",
            "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh",
            "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise",
            "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular",
            "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably",
            "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless",
            "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second",
            "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible",
            "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six",
            "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat",
            "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure",
            "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats",
            "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter",
            "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre",
            "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through",
            "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries",
            "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until",
            "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various",
            "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve",
            "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever",
            "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether",
            "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing",
            "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you",
            "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero", "null"};


    public List advertSorter(int userID) {
        String splitTokens = "\\s+|,|[.]|[?]|[!]";
        User user = userService.returnUserByID(userID);
        String education = user.getEducation();
        String company = user.getCompany();
        String city = user.getCity();
        String profession = user.getProfession();

        String input = education + " " + company + " " + city + " " + profession;
        // Split the input to words
        String[] words = input.split(splitTokens);

        // Add the words to a dictionary
        Map<String, Integer> userInfoDict = new HashMap<String, Integer>();
        //System.err.println("#### info words ####");
        // For every word in users info
        for (String word : words) {
            word = word.toLowerCase().trim();
            if (!word.isEmpty()) {
                // If the word is not a stopword
                if (!stopwordsSet.contains(word)) {
                    //System.err.println(word);
                    // We don't care about duplicate words, they will get overwritten with 0
                    userInfoDict.put(word, 0);
                }
            }
        }

        //System.err.println("################");

        List<Post> adverts = postService.getAllPublicAds(userID);
        if (adverts.size() == 0) {
            return adverts;
        }
        advertContainer = new AdvertContainer[adverts.size()];

        // For every advert
        int counter = 0;
        for (Post advert : adverts) {
            String content = advert.getContent();
            advertContainer[counter] = new AdvertContainer();
            advertContainer[counter].advert = advert;
            // Create the set to store each word
            advertContainer[counter].set = new HashSet<>();
            // Split it to words
            advertContainer[counter].contentWords = content.split(splitTokens);
            advertContainer[counter].score = 0;


            //System.err.println("# # # # Advert # # # # ");

            // Add the words to a hash set
            for (String cWord : advertContainer[counter].contentWords) {
                cWord = cWord.toLowerCase().trim();
                if (!cWord.trim().isEmpty()) {
                    if (!stopwordsSet.contains(cWord)){
                        advertContainer[counter].set.add(cWord);
                    }
                }
            }
            //System.err.println(content);
            //System.err.println("$$$$$$$$ Advert words");
            for (String s : advertContainer[counter].set) {
                //System.err.println(s);
            }
            counter++;
        }

        //System.err.println("&&&&&&&&&&&&&& Metrhma");
        // Count how many words of the users info appear in each advert
        // For every word in the user info
        for (Map.Entry<String, Integer> entry : userInfoDict.entrySet()) {
            String key = entry.getKey();
            //System.err.println("KEY: " + key);
            // For every advert
            for (int i = 0; i < adverts.size(); i++) {
                //System.err.println("  ADVERT: [" + advertContainer[i].advert.getContent().substring(0, 10) + "...]");

                if (advertContainer[i].set.contains(key)) {
                    advertContainer[i].score++;

                    //System.err.println("    SCORE INC:" + key + advertContainer[i].score);
                }
                //System.err.println("  ADVERT SCORE:" + advertContainer[i].score);

            }
            Integer value = entry.getValue();
            entry.setValue(value);
            //System.err.println(key + value.toString());
        }

        Arrays.sort(advertContainer, Collections.reverseOrder());

        List<Post> returnValue = new ArrayList<>();

        for (AdvertContainer ac : advertContainer) {
            returnValue.add(ac.advert);
            //System.err.println(ac.score);
        }

        return returnValue;
    }
}



