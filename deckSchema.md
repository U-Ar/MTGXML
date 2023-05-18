# MTG Deck Schema

## Arguments / 争点・ラフアイディア

- 最小限の記述でも許され、MTGAのデッキリストから直接変換可能な一方、やろうと思えば紙のカードの状態まで事細かに記述できるスキーマが理想
    - デッキの構成カードの状態や言語、フォイルかどうかなどが記述できると良い
        - その一方、オラクルはデッキにいちいち記述することではないので省くべきな気がする
- したがって、カードの種類を一意に識別さえできれば最低限許すべき
    - ID的な一意識別子は存在しない、強いて言うならエキスパンション略称＋コレクター番号の組み合わせだが、古いエキスパンションにそれは存在しないため不十分な説がある
    - 結局MTGのカード識別子として最も優秀なのは英語名なのかもしれない
    - 他言語だと、そもそも訳語が存在していないカードがある
- 英語名を識別子にするとして、それはそれで名前がないカード (http://mtgwiki.com/wiki/%EF%BC%BF%EF%BC%BF%EF%BC%BF%EF%BC%BF%EF%BC%BF) がUnhingedに収録されているのでそれが問題
    - 要素内を空白にすればそのカードとすればよいが、不具合との区別が難しい
    - アンダースコア5文字で登録されている場合が多いらしいが、それは便宜上であって公式じゃないんだよな...
    - とはいえ、英語名識別子に関するルールはXMLスキーマで定義できる部分ではないので、ここで考えなくても良いことではある

---

- deck要素から始まる
    - env属性でプレイ環境(紙・MTGA・MO)を指定→なくても概念上デッキとして成立するのでオプショナル
    - format属性でフォーマットを指定→なくても概念上デッキとして成立する(カジュアルならどんな遊び方でも許容される)のでオプショナル
        - 一応format属性でcasual/none/freeあたりの名前で明示できるように選択肢は用意する
        - 公式認定フォーマットをとりあえず既定の選択肢とする。過去の物も含む。( http://mtgwiki.com/wiki/%E3%83%95%E3%82%A9%E3%83%BC%E3%83%9E%E3%83%83%E3%83%88)
            - standard, block, extended, pioneer, modern, legacy, vintage, pauper, commander, two-headed, brawl, sealed, booster-draft, rochester-draft
            - MOフォーマット
                - freeform, theme, prismatic, 100cards-singleton, tribal-wars, momir-basic, cube-draft, 1v1-commander, planeswalker
            - MTGAフォーマット
                - historic, historic-brawl, alchemy, explorer, artisan shakeup
            - カジュアル変種
                - planechase, vanguard, archenemy, conspiracy-draft, oath-breaker
            疲れたのでこの辺で列挙を止める
    - スタンダードやエクステンデッド・ブロック構築のようなローテーションフォーマットやリミテッドの場合、どの時点・どのエキスパンションで構築されたかの情報が必要
        - どうやって識別できるようにする？
            - 構築された日時を書いても、国ごとにエキスパンション発売日が違う場合があるだろうから一意に定まらない
                - だいたい、デッキは構築されてから何日かプレイされ続けるものである場合が多いので、あるタイムポイント一点を決めるというのがナンセンス
            - リミテッドの場合はエキスパンション略号、ローテーションフォーマットの場合は直近スタンダードリーガルセットのエキスパンション略号で識別すると誤解がなく一意 latestExpansionがよさげ、ただリミテの場合はlatestとかじゃないので名前に分岐が生じるか
    - それとは別に構築日時の属性constructedDateはオプショナルで存在してもいいと思う
    - 大会成績などは、デッキに紐づくというよりは大会成績記述用のスキーマでdeckを囲む形で記述する方が適切そう
    - 名前nameもオプショナルであっていい、白単アグロとか書くやつ
    - とりあえずこのぐらい、備考属性があってもいいかも

---

- deck内部はmain要素とside要素とcommander要素から構成される、side要素とcommander要素はオプショナル
    - sideやcommanderは存在しないフォーマットがある

- main要素
    - メインデッキを表現する
    - num属性：総枚数 必須
- side要素
    - サイドデッキを表現する
    - num属性：総枚数 必須
- commander要素
    - 統率領域に置かれるカードを表現する
    - num属性：総枚数 必須
        - 共闘や背景、アトラクションなど複数枚が置かれうる

---

- main, side, commanderの内部は、基本的にはcardItem要素から構成される
    - ただし、sideとcommanderは微妙に要件が異なる
        - 統率者や相棒はサイドデッキ・統率領域でのみ有効になる性質
    - だから、相棒や統率者をcompanionCardItem, commanderCardItemといった形でcardItemから拡張し、
    - side内部ではcardItemとcompanionCardItemを両方許す
    - commander内部ではcardItemとcommanderCardItemを両方許す


