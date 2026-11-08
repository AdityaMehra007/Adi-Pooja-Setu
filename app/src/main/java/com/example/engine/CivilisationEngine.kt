package com.example.engine

data class CivilisationEntry(
    val id: String,
    val pillar: String, // VEDAS_UPANISHADS, TANTRA_AGHORA, ANCIENT_SCIENCES, PHILOSOPHY_DARSHANAS
    val title: String,
    val subtitle: String,
    val locusCitation: String,
    val sanskritOriginal: String,
    val transliteration: String,
    val epistemicCode: String,
    val primaryTextStatement: String,
    val traditionalCommentary: String,
    val modernScientificEvaluation: String,
    val safetyNotice: String? = null
)

object CivilisationEngine {

    val ARTICLES = listOf(
        CivilisationEntry(
            id = "civ-nasadiya",
            pillar = "VEDAS_UPANISHADS",
            title = "Nasadiya Sukta: Rigvedic Cosmological Inquiry & Epistemic Humility",
            subtitle = "Rigveda Mandala 10, Sukta 129 • Rishi Prajapati Parameshthin • Trishtubh Meter",
            locusCitation = "Rigveda Samhita 10.129.1–7 (Shakala Shakha, Poona Critical Edition)",
            sanskritOriginal = "नासदासीन्नो सदासीत्तदानीं नासीद्रजो नो व्योमा परो यत् । को अद्धा वेद क इह प्र वोचत् कुत आजाता कुत इयं विसृष्टिः ॥",
            transliteration = "nāsadāsīnno sadāsīttadānīṁ nāsīdrajo no vyomā paro yat | ko addhā veda ka iha pra vocat kuta ājātā kuta iyaṁ visṛṣṭiḥ ||",
            epistemicCode = "CAT-A • Primary Textual Source",
            primaryTextStatement = "Inquires into the state prior to the manifestation of existence (Sat) and non-existence (Asat), space, death, and immortality, concluding with profound philosophical skepticism: 'Who truly knows? Even the overseer in the highest heaven—perhaps He knows, or perhaps even He does not know.'",
            traditionalCommentary = "Sayanacharya and Shankaracharya interpret 'nāsadāsīt' through the lens of Maya/Avyakta (unmanifest nature) preceding cosmic projection, establishing philosophical Jijnasa (inquiry).",
            modernScientificEvaluation = "Philosophers of science recognize RV 10.129 as one of humanity's earliest documented expressions of non-dogmatic, open cosmological inquiry."
        ),
        CivilisationEntry(
            id = "civ-tantra-aghora",
            pillar = "TANTRA_AGHORA",
            title = "Aghora Philosophy, Shaiva Non-Duality & Demystification of 'Black Magic'",
            subtitle = "Svacchanda Tantra, Tantraloka of Abhinavagupta & Kina Ram Tradition",
            locusCitation = "Abhinavagupta's Tantraloka (Ahnika 1 & 29) & Svacchanda Tantra (KSTS)",
            sanskritOriginal = "अघोरेभ्योऽथ घोरेभ्यो घोरघोरतरेभ्यः । सर्वेभ्यः सर्वशर्वेभ्यो नमस्ते अस्तु रुद्ररूपेभ्यः ॥",
            transliteration = "aghorebhyo'tha ghorebhyo ghoraghoratarebhyaḥ | sarvebhyaḥ sarvaśarvebhyo namaste astu rudrarūpebhyaḥ ||",
            epistemicCode = "CAT-B • Historical Scholarship",
            primaryTextStatement = "Aghora literally denotes 'non-terrifying' or 'luminous awareness beyond fear.' Originating as one of the 5 faces of Shiva in Taittiriya Aranyaka 10.43, it teaches radical non-duality—overcoming social constructs of disgust, fear, and aversion.",
            traditionalCommentary = "In the Kina Ram tradition and Trika Shaivism, the cremation ground (Smashana) symbolizes burning away ego (Ahamkara) and worldly attachments (Pasha) to recognize universal consciousness everywhere.",
            modernScientificEvaluation = "Anthropological research demonstrates that popular media depictions of Aghoris as 'black magicians' are sensationalized folklore. Fraudulent practitioners exploit psychological fear (nocebo effect) to extort money.",
            safetyNotice = "SATYA ETHICAL BOUNDARY: PoojaSetu provides educational history only. Rituals claiming to curse others or demand money for 'black magic removal' are strictly banned."
        ),
        CivilisationEntry(
            id = "civ-aryabhata",
            pillar = "ANCIENT_SCIENCES",
            title = "Aryabhata: Earth's Axial Rotation, Sine Tables & Pi Approximation (499 CE)",
            subtitle = "Aryabhatiya (Ganitapada & Golapada) & Kerala School Calculus Lineage",
            locusCitation = "Aryabhatiya, Golapada v. 9 & Ganitapada v. 10",
            sanskritOriginal = "अनुलोमगतिर्नौस्थः पश्यत्यचलं विलोमगं यद्वत् । अचलानि भानि तद्वत् समपश्चिमगानि लङ्कायाम् ॥",
            transliteration = "anulomagatirnausthaḥ paśyatyacalaṁ vilomagaṁ yadvat | acalāni bhāni tadvat samapaścimagāni laṅkāyām ||",
            epistemicCode = "CAT-E • Empirical Science & Mathematics",
            primaryTextStatement = "In Golapada 9, Aryabhata explains that just as a passenger in a forward-moving boat sees stationary shore objects moving backward, an observer on Earth sees stationary stars moving westward due to the axial rotation of Earth. In Ganitapada 10, he computes pi as 62832 / 20000 = 3.1416, calling it 'Asanna' (approximate).",
            traditionalCommentary = "Later Kerala school astronomers (Madhava of Sangamagrama, Nilakantha Somayaji) expanded this into infinite series for sine, cosine, and arctangent with geometric proofs (Upapatti).",
            modernScientificEvaluation = "Independently verified by historians of mathematics: Aryabhata's pi is accurate to 4 decimal places, and his sidereal rotation period (23h 56m 4.1s) is within 0.09s of modern measurements."
        ),
        CivilisationEntry(
            id = "civ-sushruta",
            pillar = "ANCIENT_SCIENCES",
            title = "Sushruta Samhita: Plastic Surgery (Rhinoplasty) & 121 Surgical Instruments",
            subtitle = "Sushruta Samhita, Sutrasthana Ch. 16 • Shalya Tantra (Surgical Sciences)",
            locusCitation = "Sushruta Samhita, Sutrasthana 16.27–31 (Kashi Sanskrit Series)",
            sanskritOriginal = "नासासंधानमथ वक्ष्यामि—नासाप्रमाणं प्रथमं पत्रं संछिद्य तद्बलेन कपोलाद् मांसमुत्कृत्य नासिकायां संदध्यात् ॥",
            transliteration = "nāsāsaṁdhānamatha vakṣyāmi—nāsāpramāṇaṁ prathamaṁ patraṁ saṁchidya tadbalena kapolād māṁsamutkṛtya nāsikāyāṁ saṁdadhyāt ||",
            epistemicCode = "CAT-E • Empirical Science & Surgery",
            primaryTextStatement = "Describes the precise pedicle cheek/forehead flap method to reconstruct severed noses (rhinoplasty), suturing with fine fibers, antiseptic cleansing with licorice and ghee, and inserting hollow castor stalks for respiration during healing.",
            traditionalCommentary = "Ashtanga Sangraha and later commentaries emphasize strict surgical hygiene, wound care, and training on gourds, leather water bags, and animal carcasses.",
            modernScientificEvaluation = "Published in Gentleman's Magazine (1794) and recognized by the British Royal College of Surgeons as the direct foundation of modern plastic reconstructive surgery."
        ),
        CivilisationEntry(
            id = "civ-vedic-swara",
            pillar = "VEDAS_UPANISHADS",
            title = "Vedic Swaras & The 8 Ashtavikritis Recitation Checksums",
            subtitle = "Panini Shiksha, Rigveda Pratishakhya & Combinatorial Phonetic Integrity",
            locusCitation = "Rigveda Pratishakhya & Paniniya Shiksha (Udatta, Anudatta, Svarita)",
            sanskritOriginal = "उच्चैरुदात्तः । नीचैरनुदात्तः । समाहारः स्वरितः ॥ जटा माला शिखा रेखा ध्वजो दण्डो रथो घनः — अष्टौ विकृतयः प्रोक्ताः ॥",
            transliteration = "uccairudāttaḥ | nīcairanudāttaḥ | samāhāraḥ svaritaḥ || jaṭā mālā śikhā rekhā dhwajo daṇḍo ratho ghanaḥ — aṣṭau vikṛtayaḥ proktāḥ ||",
            epistemicCode = "CAT-A • Primary Linguistic Witness",
            primaryTextStatement = "Defines the 3 musical pitch registers (Udatta = high, Anudatta = low, Svarita = circumflex) and the 8 algorithmic permuted recitation methods: Jata, Mala, Shikha, Rekha, Dhwaja, Danda, Ratha, and Ghana.",
            traditionalCommentary = "A Ghanapathi chants words in recursive permutations: 1-2-2-1-1-2-3-3-2-1-1-2-3, ensuring not a single syllable, vowel length, or pitch accent could be altered across millennia without immediate acoustic detection.",
            modernScientificEvaluation = "Computer scientists and phoneticians recognize the Ashtavikritis as an oral checksum error-detecting code functioning like cryptographic hashes long before written books."
        )
    )
}
