package com.cll.wenda;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import org.junit.Test;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/11/2
 */
public class HanLPTest {


    /**
     *提取关键词
     *
     * 内部采用TextRankKeyword实现，
     * 用户可以直接调用TextRankKeyword.getKeywordList(document, size)
     *
     * 算法详解： http://www.hankcs.com/nlp/textrank-algorithm-to-extract-the-keywords-java-implementation.html
     */
    @Test
    public void testKeyWords(){

        String content = "那里安全吗？\n" +
                "\n" +
                "我觉得不安全只是我的主观猜想而已。最主要还是听别人说，在纪录片，电影里面看到阿国的北方山区，似乎可以践踏各种人间法律。而政府也没有那么多财力和精力来把文明制度都传播过去，所以北方山区似乎就被遗漏在历史的进步之外了。\n" +
                "\n" +
                "在苏联时代，阿尔巴尼亚历史上一个极具争议的领导人——霍查(Hoxha)，对国家进行了各种大变革，精神和物质上的都有，而且影响巨大。后人争论非常多。\n" +
                "\n" +
                "其中有两项政策是采用了国家警察的血腥清洗。\n" +
                "\n" +
                "一项是禁止所有Kanun的实行（我下面来解释），另一项是禁止所有宗教，并宣布阿尔巴尼亚为世界上第一个无神论国家。\n" +
                "\n" +
                "这两项政策本来是为了制止阿国多年来的各种家族世仇（Blood feuds），但是由于苏联政府的垮台，仇恨的积压与宣泄更加变本加厉。\n" +
                "\n" +
                "\n" +
                "什么是Kanun？\n" +
                "\n" +
                "Kanun是指的一系列创建于中世纪的阿尔巴尼亚传统法律，几百年来每一代人都是口头传述，所以地方强权都根据需要在往里面添油加醋地制造有利于自己的条款。\n" +
                "\n" +
                "直到上个世纪初才有人把这些律令归总在书本里面，一共加起来有1200多条。\n" +
                "\n" +
                "其中最粗暴的一条便是：如果自己家庭男性亲人被杀，可以杀仇人家里一名男性或两名女性（因为女性只算半条人民）。\n" +
                "\n" +
                "而且Kanun里面非常臭名昭著的便是对女性的贬低和打压。\n" +
                "\n" +
                "规定女性不能独立到屋子外面去，不能唱歌，喝酒，大声说话；女人的婚姻只能由父亲与家中其他男性来决定；结婚之后女方终生不能提出离婚；女方只能做两件事情：生孩子与抚养孩子；如果在家族世仇的厮杀中，一个女人被杀了，家族血仇记账簿上只能算半个人；一个处女的价格是12头牛等；女人不能继承和持有任何财产，除了身上的衣服，什么都不能有。\n" +
                "\n" +
                "在苏联时代，霍查虽然自己品行也不算很好，但是他对Kanun的严厉禁止，甚至有时候要出动国家警察来端掉奉行Kanun的家族，也是很大快人心的。\n" +
                "\n" +
                "同时霍查对各种宗教也镇压，持有宗教印刷品就可以招致10年徒刑，主动传教就可以枪决。有世仇的家族之间，便利用这个机会相互举报揭发对方，利用国家警察来铲除异己，但同时结下的仇恨更加深刻，只等有朝一日政策被取缔，他们便能抄家伙血洗仇恨。\n" +
                "\n";
        List<String> keywordList = HanLP.extractKeyword(content, 6);
        System.out.println(keywordList);
    }

    /**
     *自动摘要
     *
     * 内部采用TextRankSentence实现，
     * 用户可以直接调用TextRankSentence.getTopSentenceList(document, size)
     *
     * 算法详解: http://www.hankcs.com/nlp/textrank-algorithm-java-implementation-of-automatic-abstract.html
     */
    @Test
    public void testSummary(){

        String document = "一、策划背景\n" +
                "1.发展历程\n" +
                "虾米音乐（app）是虾米网为 android 操作系统打造的免费的移动音乐应用，不仅提供 流畅度高、音乐品质高的无线音乐外，更是国内首家推出离线模式的应用。虾米音乐以点对 点传输和社区互动文化为核心，使用户在音乐交流分享中获得了自己独特的音乐体验。并在 2013 年被阿里巴巴并购，并期望通过与独立唱片公司和独立音乐人合作，改变音乐产业链 发展，推动付费模式发展（引自浙江大学某硕士论文）。\n" +
                "2.存在问题\n" +
                "虾米音乐作为一款品质较为优良的音乐 app，其所占市场份额却比较小，用户粘度降低， 用户流失较为严重，有着很大的营销推广空间。\n" +
                "3.策划目标\n" +
                "根据虾米音乐的发展现状，其营销方案的主要目标为增大用户使用量，即增加市场份额， 提高用户黏性和忠诚度，增大用户使用频率，在老用户留存的基础上，积极拉取新用户，逐 步提高市场知名度及其影响力。 策划目标可主要从三个方面来讲： 一是突出虾米竞争优势进行营销推广扩展知名度， 二是通过不断发现问题推动虾米自身的更新迭代，从而更好提升用户体验， 三是与阿里联合发展，推动音乐整体产业链发展，联合各方资源打造积极的音乐生态圈。\n" +
                "二、市场分析\n" +
                "1.市场细分\n" +
                "虾米音乐可以按照年龄、学历、收入、兴趣等因素对音乐市场进行细分。如青年、中年、 老年，本科以下、本科及以上，低、中、高收入，小众、大众音乐等等细分类型。\n" +
                "4\n" +
                "2.目标市场\n" +
                "经市场调查及相关数据的手机收集，可将虾米音乐的目标用户定为 18-35 岁的具有本科 及以上学历的文艺青年、城市白领、音乐爱好者等年轻群体。\n" +
                "三、战略综述\n" +
                "1.战略目标\n" +
                "（1）增加用户粘性及使用频率，抢占市场份额\n" +
                "①打造更加流畅，更加简洁美观的用户界面 ②积极打造社区氛围，增强音乐社区的整体互动性 ③着重满足用户的个性化需求，丰富用户参与接口\n" +
                "（2）强化品牌形象，增加辨识度\n" +
                "①积极开展各种形式的推广宣传活动 ②不断深化细化产品细节，提高虾米在用户心中的格调水平 ③突显虾米特色，增强虾米的辨识度\n" +
                "（3）整合音乐资源，整体联合发展\n" +
                "①努力建立与阿里巴巴其它产品间的关联性 ②积极发展音乐周边 ③联动配合，强强合作\n" +
                "2.营销原则\n" +
                "①成本控制 ②品牌管理 ③用户至上\n" +
                "5\n" +
                "④联合发展\n" +
                "3.营销方案综述\n" +
                "以打造积极的品牌形象为核心，通过优化虾米音乐 app 用户界面，提高用户操作流畅性， 并提供相应的用户自主操作接口及精准的音乐推荐算法，满足用户的个性化需求，并通过一 定营销手段积极活跃音乐社区的氛围，不断提高用户的使用频率，加以辅助实时的内容更新 推荐，持续增加用户粘性，从而最终建立虾米音乐在用户心中逼格高、有趣、时尚、专业的 音乐交流社区的品牌形象。并借此机遇，积极发展与阿里巴巴旗下其他产品及相关音乐周边 品牌的关系，通过资源共享、关联消费等“互通有无”的方式实现双方乃至整个音乐生态圈 的共同发展。\n" +
                "四、虾米营销策划方案\n" +
                "1.GUI 改进\n" +
                "①改进用户界面，例如仿照网易云音乐主页的侧栏（需右滑），将用户设置的相关操作 放入侧栏，使得用户可以很方便快捷得按照自己的想法进行个性化的设置，彰显个人特征。 ②削减部分个性化推荐版块，尤其是“我的音乐”模块中的“歌单推荐”可以删掉，太 多重复的推荐可能并不会提升用户的好感度，反而会因推荐的不准确以及多模块堆积造成页 面的杂乱无序，从而使得用户体验降低。\n" +
                "2.社区互动\n" +
                "①定期推出音乐主题\n" +
                "1）这里的主题可以有多种形式，例如“一歌一故事”，这里的故事既可以是歌曲创 作者、歌曲演绎者的故事，也可以是用户结合自身经历用心创作的故事，用户在听歌的 同时，还可以在各种题材、各种体制的故事中体会各色人等的情感经历，寻找自己的共 鸣，比单纯在歌曲评论中的几行小字可能感触更加深刻。最后可对获得点赞数最多的用 户进行奖励，提供与该歌演唱者互动的机会，以此激励用户的分享创作。 2）或者“一歌一旅行”，鼓励用户将和歌曲有关的旅行地点、旅行经历等奇特的有 趣的事分享出来，以此激励其它用户想要出发前去的欲望，将音乐和旅行结合在一起， 不再仅限于听觉和心灵上的享受，更可以感受到来自自然的视觉冲击，也可以以此带动 某些旅游产业的发展，实现音乐周边产品的联合发展。\n" +
                "6\n" +
                "3）或者“十歌觅知音”，当用户报名参与该活动时，填写并提交十首自己喜欢的歌 曲，并由虾米音乐平台选择某一个时间节点，将所有用户的歌曲信息全部展示在该活动 内容下，用户可以在各个用户的参与信息中，寻找和自己的音乐品味爱好最相近的用户， 相互关注，彼此交流。而用户因每天的心情不同所侧重的歌曲必然也不尽相同，这也就 说明了该活动的可持续开展性，以此来活跃音乐社区的整体氛围。 4）还可以设置一个专区，供用户提供主题建议，类似于微博网红“微博搞笑排行榜” 的每日话题的形式。\n" +
                "②虾友线下聚会\n" +
                "可不定期举办虾友线下主题交流活动，以虾米音乐为旗号，将线上的虾友汇聚到线 下，并将线下活动的图片、视频内容上传到虾米平台，并由用户进行评论投票，将呼声 最高的“虾友线下聚会”推荐为本季最佳，并对参与其中的虾米用户进行相应的奖励。 由此将线上线下充分结合，更好地增强用户对虾米的感情粘度。\n";
        List<String> sentenceList = HanLP.extractSummary(document, 20);
        System.out.println(sentenceList);
    }


    /**
     * 短语提取
     *
     * 内部采用MutualInformationEntropyPhraseExtractor实现，
     * 用户可以直接调用MutualInformationEntropyPhraseExtractor.extractPhrase(text, size)。
     *
     * 算法详解: http://www.hankcs.com/nlp/extraction-and-identification-of-mutual-information-about-the-phrase-based-on-information-entropy.html
     */
    @Test
    public void testPhrase(){
        String text = "算法工程师\n" +
                "算法（Algorithm）是一系列解决问题的清晰指令，也就是说，能够对一定规范的输入，在有限时间内获得所要求的输出。如果一个算法有缺陷，或不适合于某个问题，执行这个算法将不会解决这个问题。不同的算法可能用不同的时间、空间或效率来完成同样的任务。一个算法的优劣可以用空间复杂度与时间复杂度来衡量。算法工程师就是利用算法处理事物的人。\n" +
                "\n" +
                "1职位简介\n" +
                "算法工程师是一个非常高端的职位；\n" +
                "专业要求：计算机、电子、通信、数学等相关专业；\n" +
                "学历要求：本科及其以上的学历，大多数是硕士学历及其以上；\n" +
                "语言要求：英语要求是熟练，基本上能阅读国外专业书刊；\n" +
                "必须掌握计算机相关知识，熟练使用仿真工具MATLAB等，必须会一门编程语言。\n" +
                "\n" +
                "2研究方向\n" +
                "视频算法工程师、图像处理算法工程师、音频算法工程师 通信基带算法工程师\n" +
                "\n" +
                "3目前国内外状况\n" +
                "目前国内从事算法研究的工程师不少，但是高级算法工程师却很少，是一个非常紧缺的专业工程师。算法工程师根据研究领域来分主要有音频/视频算法处理、图像技术方面的二维信息算法处理和通信物理层、雷达信号处理、生物医学信号处理等领域的一维信息算法处理。\n" +
                "在计算机音视频和图形图像技术等二维信息算法处理方面目前比较先进的视频处理算法：机器视觉成为此类算法研究的核心；另外还有2D转3D算法(2D-to-3D conversion)，去隔行算法(de-interlacing)，运动估计运动补偿算法(Motion estimation/Motion Compensation)，去噪算法(Noise Reduction)，缩放算法(scaling)，锐化处理算法(Sharpness)，超分辨率算法(Super Resolution),手势识别(gesture recognition),人脸识别(face recognition)。\n" +
                "在通信物理层等一维信息领域目前常用的算法：无线领域的RRM、RTT，传送领域的调制解调、信道均衡、信号检测、网络优化、信号分解等。\n" +
                "另外数据挖掘、互联网搜索算法也成为当今的热门方向。\n" +
                "算法工程师逐渐往人工智能方向发展。";
        List<String> phraseList = HanLP.extractPhrase(text, 10);
        System.out.println(phraseList);
    }


    /**
     * 文本推荐
     *
     *在搜索引擎的输入框中，用户输入一个词，搜索引擎会联想出最合适的搜索词，HanLP实现了类似的功能。
     *可以动态调节每种识别器的权重
     */
    @Test
    public void testSuggester(){
        Suggester suggester = new Suggester();
        String[] titleArray =
                (
                        "威廉王子发表演说 呼吁保护野生动物\n" +
                                "《时代》年度人物最终入围名单出炉 普京马云入选\n" +
                                "“黑格比”横扫菲：菲吸取“海燕”经验及早疏散\n" +
                                "日本保密法将正式生效 日媒指其损害国民知情权\n" +
                                "英报告说空气污染带来“公共健康危机”"
                ).split("\\n");
        for (String title : titleArray)
        {
            suggester.addSentence(title);
        }

        System.out.println(suggester.suggest("发言", 1));       // 语义
        System.out.println(suggester.suggest("危机公共", 1));   // 字符
        System.out.println(suggester.suggest("mayun", 1));      // 拼音
    }


    /**
     * 机构名识别
     */
    @Test
    public void testOrganizationRecognize(){
        String[] testCase = new String[]{
                "我在上海林原科技有限公司兼职工作，",
                "我经常在台川喜宴餐厅吃饭，",
                "偶尔去地中海影城看电影。",
        };
        Segment segment = HanLP.newSegment().enableOrganizationRecognize(true);
        for (String sentence : testCase)
        {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }


    /**
     * 地名识别
     */
    @Test
    public void testPlaceRecognize(){
        String[] testCase = new String[]{
                "武胜县新学乡政府大楼门前锣鼓喧天",
                "蓝翔给宁夏固原市彭阳县红河镇黑牛沟村捐赠了挖掘机",
        };
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        for (String sentence : testCase)
        {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }

    /**
     * 中国人名识别
     */
    @Test
    public void testNameRecognize(){
        String[] testCase = new String[]{
                "签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。",
                "王国强、高峰、汪洋、张朝阳光着头、韩寒、小四",
                "张浩和胡健康复员回家了",
                "王总和小丽结婚了",
                "编剧邵钧林和稽道青说",
                "这里有关天培的有关事迹",
                "龚学平等领导,邓颖超生前",
        };
        Segment segment = HanLP.newSegment().enableNameRecognize(true);
        for (String sentence : testCase)
        {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }


    /**
     * 音译人名识别
     */
    @Test
    public void testTranslatedNameRecognize(){
        String[] testCase = new String[]{
                "一桶冰水当头倒下，微软的比尔盖茨、Facebook的扎克伯格跟桑德博格、亚马逊的贝索斯、苹果的库克全都不惜湿身入镜，这些硅谷的科技人，飞蛾扑火似地牺牲演出，其实全为了慈善。",
                "世界上最长的姓名是简森·乔伊·亚历山大·比基·卡利斯勒·达夫·埃利奥特·福克斯·伊维鲁莫·马尔尼·梅尔斯·帕特森·汤普森·华莱士·普雷斯顿。",
        };
        Segment segment = HanLP.newSegment().enableTranslatedNameRecognize(true);
        for (String sentence : testCase)
        {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }


    /**
     * 日本人名识别
     */
    @Test
    public void testJapaneseNameRecognize(){
        String[] testCase = new String[]{
                "北川景子参演了林诣彬导演的《速度与激情3》",
                "林志玲亮相网友:确定不是波多野结衣？",
        };
        Segment segment = HanLP.newSegment().enableJapaneseNameRecognize(true);
        for (String sentence : testCase)
        {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }

    /**
     * 极速分词是词典最长分词，速度极其快，精度一般。
     */
    @Test
    public void testSpeedTokenizer(){
        String text = "江西鄱阳湖干枯，中国最大淡水湖变成大草原";
        System.out.println(SpeedTokenizer.segment(text));
        long start = System.currentTimeMillis();
        int pressure = 1000000;
        for (int i = 0; i < pressure; ++i)
        {
            SpeedTokenizer.segment(text);
        }
        double costTime = (System.currentTimeMillis() - start) / (double)1000;
        System.out.printf("分词速度：%.2f字每秒", text.length() * pressure / costTime);

    }


    /**
     * 索引分词IndexTokenizer是面向搜索引擎的分词器，
     * 能够对长词全切分，另外通过term.offset可以获取单词在文本中的偏移量。
     */
    @Test
    public void testIndexTokenizer(){
        List<Term> termList = IndexTokenizer.segment("主副食品");
        for (Term term : termList)
        {
            System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
        }
    }

    /**
     *
     * 拼音转换
     *
     *
     *HanLP不仅支持基础的汉字转拼音，还支持声母、韵母、音调、音标和输入法首字母首声母功能。
     *HanLP能够识别多音字，也能给繁体中文注拼音。
     *最重要的是，HanLP采用的模式匹配升级到AhoCorasickDoubleArrayTrie，性能大幅提升，能够提供毫秒级的响应速度！

     */
    @Test
    public void testConvertToPinyin(){
        String text = "重载不是重任";
        List<Pinyin> pinyinList = HanLP.convertToPinyinList(text);
        System.out.print("原文,");
        for (char c : text.toCharArray())
        {
            System.out.printf("%c,", c);
        }
        System.out.println();

        System.out.print("拼音（数字音调）,");
        for (Pinyin pinyin : pinyinList)
        {
            System.out.printf("%s,", pinyin);
        }
        System.out.println();

        System.out.print("拼音（符号音调）,");
        for (Pinyin pinyin : pinyinList)
        {
            System.out.printf("%s,", pinyin.getPinyinWithToneMark());
        }
        System.out.println();

        System.out.print("拼音（无音调）,");
        for (Pinyin pinyin : pinyinList)
        {
            System.out.printf("%s,", pinyin.getPinyinWithoutTone());
        }
        System.out.println();

        System.out.print("声调,");
        for (Pinyin pinyin : pinyinList)
        {
            System.out.printf("%s,", pinyin.getTone());
        }
        System.out.println();

        System.out.print("声母,");
        for (Pinyin pinyin : pinyinList)
        {
            System.out.printf("%s,", pinyin.getShengmu());
        }
        System.out.println();

        System.out.print("韵母,");
        for (Pinyin pinyin : pinyinList)
        {
            System.out.printf("%s,", pinyin.getYunmu());
        }
        System.out.println();

        System.out.print("输入法头,");
        for (Pinyin pinyin : pinyinList)
        {
            System.out.printf("%s,", pinyin.getHead());
        }
        System.out.println();
    }

    /**
     * 简繁转换
     *
     * HanLP能够识别简繁分歧词，比如打印机=印表機。
     * 许多简繁转换工具不能区分“以后”“皇后”中的两个“后”字，HanLP可以。
     */
    @Test
    public void testConvertToTraditionalChinese(){
        System.out.println(HanLP.convertToTraditionalChinese("用笔记本电脑写程序"));
        System.out.println(HanLP.convertToSimplifiedChinese("「以後等妳當上皇后，就能買士多啤梨慶祝了」"));
    }
}
