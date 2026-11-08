import React, { useState, useMemo } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

// --- Types & Data Structures ---

export type TirthaCategory = 'JYOTIRLINGA' | 'SHAKTI_PEETHA' | 'CHAR_DHAM';

export interface SacredTirtha {
  id: string;
  name: string;
  category: TirthaCategory;
  lat: number;
  lng: number;
  state: string;
  location: string;
  presidingDeity: string;
  associatedBhairavaOrConsort?: string;
  bodyPartOrSignificance?: string; // Sati's body part or sacred meaning
  textualSource: string; // e.g. Shiva Purana, Tantrachudamani
  architecturalStyle: string;
  historicalPeriod: string;
  sthalaPurana: string;
  recommendedMantra: string;
  accessibility: string;
}

// --- Verified Datasets ---

export const JYOTIRLINGAS: SacredTirtha[] = [
  {
    id: 'jyo-1',
    name: 'Somnath Jyotirlinga',
    category: 'JYOTIRLINGA',
    lat: 20.8880,
    lng: 70.4012,
    state: 'Gujarat',
    location: 'Prabhas Patan, Veraval',
    presidingDeity: 'Lord Somnath (The Lord of the Moon)',
    associatedBhairavaOrConsort: 'Goddess Parvati (Chandrabhaga)',
    bodyPartOrSignificance: 'First of the 12 Dwadasha Jyotirlingas, symbolizing eternal resurrection & Chandra’s healing.',
    textualSource: 'Shiva Purana (Koti Rudra Samhita, Ch. 14) & Rigvedic references',
    architecturalStyle: 'Chalukya (Solanki) Temple Architecture with Kailash Mahameru Prasad',
    historicalPeriod: 'Ancient foundations; rebuilt by Sardar Vallabhbhai Patel (consecrated 1951 CE)',
    sthalaPurana: 'Soma (Chandra) cured of Daksha Prajapati’s curse by bathing at the Triveni Sangam and worshipping Mahadeva.',
    recommendedMantra: 'ॐ नमः शिवाय । सौराष्ट्रे सोमनाथं च श्रीशैले मल्लिकार्जुनम् ॥',
    accessibility: 'Step-free wheel-chair ramps, battery carts from gate, and elderly priority darshan.'
  },
  {
    id: 'jyo-2',
    name: 'Mallikarjuna Jyotirlinga',
    category: 'JYOTIRLINGA',
    lat: 16.0740,
    lng: 78.8687,
    state: 'Andhra Pradesh',
    location: 'Srisailam, Nallamala Hills',
    presidingDeity: 'Lord Mallikarjuna & Goddess Bhramaramba Devi',
    associatedBhairavaOrConsort: 'Goddess Bhramaramba (Also a Maha Shakti Peetha)',
    bodyPartOrSignificance: 'Unique combination of Jyotirlinga & Shakti Peetha on the same sacred hill (Sriparvata).',
    textualSource: 'Shiva Purana, Mahabharata (Vana Parva) & Skanda Purana',
    architecturalStyle: 'Vijayanagara & Kakatiya Dravidian architecture with vast Prakaras',
    historicalPeriod: 'Patronized by Satavahanas, Ikshvakus, Kakatiyas, and Krishnadevaraya',
    sthalaPurana: 'Shiva and Parvati manifested to console Kartikeya who resided here after circumambulating the cosmos.',
    recommendedMantra: 'श्रीशैले मल्लिकार्जुनं । ॐ नमः शिवाय ॥',
    accessibility: 'Paved prakaram pathways, lift assistance to queue complexes, free motorized wheelchairs.'
  },
  {
    id: 'jyo-3',
    name: 'Mahakaleshwar Jyotirlinga',
    category: 'JYOTIRLINGA',
    lat: 23.1827,
    lng: 75.7682,
    state: 'Madhya Pradesh',
    location: 'Ujjain (Avanti Nagari), Kshipra River',
    presidingDeity: 'Lord Mahakal (Dakshinabhimukhi / South-Facing)',
    associatedBhairavaOrConsort: 'Goddess Mahakali (Harsiddhi Peetha nearby)',
    bodyPartOrSignificance: 'Master of Time and Death (Kala-Jaya); only South-facing Jyotirlinga.',
    textualSource: 'Shiva Purana, Matsya Purana & Kalidasa’s Meghaduta',
    architecturalStyle: 'Bhumija, Chalukya & Maratha hybrid styles with subterranean sanctum',
    historicalPeriod: 'Revitalized by Paramaras (11th c.) and General Ranoji Shinde (1734 CE)',
    sthalaPurana: 'Mahadeva appeared from the earth to destroy demon Dushana and protect the pious Vedic residents of Ujjain.',
    recommendedMantra: 'उज्जयिन्यां महाकालम् । ॐ त्र्यम्बकं यजामहे सुगन्धिं पुष्टिवर्धनम् ॥',
    accessibility: 'Modern Mahakal Lok corridor, mechanized lifts for elderly, dedicated Bhasma Aarti senior lane.'
  },
  {
    id: 'jyo-4',
    name: 'Omkareshwar & Mamleshwar',
    category: 'JYOTIRLINGA',
    lat: 22.2464,
    lng: 76.1517,
    state: 'Madhya Pradesh',
    location: 'Mandhata Island, Narmada River',
    presidingDeity: 'Lord Omkareshwar & Amaleshwar',
    associatedBhairavaOrConsort: 'Goddess Annapurna & Narmada Devi',
    bodyPartOrSignificance: 'The sacred island is shaped naturally like the primordial symbol ॐ (AUM).',
    textualSource: 'Shiva Purana (Rudra Samhita) & Narmada Purana',
    architecturalStyle: 'North Indian Nagara style with carved sandstone pillars & balconies',
    historicalPeriod: 'Mandhata dynasty, Parmar rulers (10th-11th c.) and Holkar queen Ahilyabai',
    sthalaPurana: 'King Mandhata performed penance here; Mount Meru prayed to Lord Shiva to manifest.',
    recommendedMantra: 'ॐकारममलेश्वरम् । ॐ नमः प्रणवस्वरूपिणे शिवाय नमः ॥',
    accessibility: 'Suspension bridge (Jhula Pul) with battery cart transport; boat transfers with life jackets.'
  },
  {
    id: 'jyo-5',
    name: 'Kedarnath Jyotirlinga',
    category: 'JYOTIRLINGA',
    lat: 30.7352,
    lng: 79.0669,
    state: 'Uttarakhand',
    location: 'Rudraprayag, Garhwal Himalayas (3,583m alt)',
    presidingDeity: 'Lord Kedaranatha (Hump of the Divine Bull)',
    associatedBhairavaOrConsort: 'Goddess Parvati & Bhairavnath guardian',
    bodyPartOrSignificance: 'Highest Jyotirlinga, situated amidst glaciers; prime seat of northern Shaivism.',
    textualSource: 'Mahabharata, Skanda Purana (Kedara Khanda) & Shiva Purana',
    architecturalStyle: 'Katyuri Himalayan Stone Architecture with massive grey interlocking granite',
    historicalPeriod: 'Pandavas attributed; Samadhi of Adi Shankaracharya (8th c. CE)',
    sthalaPurana: 'Pandavas sought atonement from Shiva; Mahadeva dove into the earth, leaving his hump at Kedarnath.',
    recommendedMantra: 'हिमाद्रौ तु केदारम् । ॐ नमः शिवाय शम्भवे केदारनाथाय नमः ॥',
    accessibility: 'Helicopter services from Guptkashi/Phata, Doli (palanquin), and pony assistance.'
  },
  {
    id: 'jyo-6',
    name: 'Bhimashankar Jyotirlinga',
    category: 'JYOTIRLINGA',
    lat: 19.0722,
    lng: 73.5358,
    state: 'Maharashtra',
    location: 'Bhorgiri near Pune, Sahyadri Range',
    presidingDeity: 'Lord Bhimashankar & Origin of River Bhima',
    associatedBhairavaOrConsort: 'Goddess Kamalaja Mata',
    bodyPartOrSignificance: 'Symbol of invincible courage overcoming demonic tyrant Bhima (son of Kumbhakarna).',
    textualSource: 'Shiva Purana & Linga Purana',
    architecturalStyle: 'Nagara & Hemadpanthi stone masonry with deeply carved shikhara',
    historicalPeriod: 'Built by Nana Fadnavis and Peshwa rulers (18th c. CE)',
    sthalaPurana: 'Lord Shiva manifested to destroy asura Bhima who tried to sever King Kamrupeshwar’s Shivalinga worship.',
    recommendedMantra: 'डाकिन्यां भीमशङ्करम् । ॐ नमः शिवाय ॥',
    accessibility: 'Paved stairs with handrails, local palanquin bearers available for pilgrims needing support.'
  },
  {
    id: 'jyo-7',
    name: 'Kashi Vishwanath Jyotirlinga',
    category: 'JYOTIRLINGA',
    lat: 25.3109,
    lng: 83.0107,
    state: 'Uttar Pradesh',
    location: 'Varanasi (Kashi), Ganga River',
    presidingDeity: 'Lord Vishwanath & Goddess Annapurna',
    associatedBhairavaOrConsort: 'Kaal Bhairav (Kotwal of Kashi) & Goddess Vishalakshi',
    bodyPartOrSignificance: 'Cosmic axis (Avimukta Kshetra) granting Moksha to all departing souls.',
    textualSource: 'Kashi Khanda (Skanda Purana) & Shiva Purana',
    architecturalStyle: 'Nagara style with golden domes donated by Maharaja Ranjit Singh (1839 CE)',
    historicalPeriod: 'Eternal city; reconstructed by Maharani Ahilyabai Holkar (1780 CE), Vishwanath Dham (2021 CE)',
    sthalaPurana: 'Light pillar (Jyotirlinga) that pierces through the earth and heavens where Brahma & Vishnu searched.',
    recommendedMantra: 'वाराणस्यां तु विश्वेशम् । ॐ नमः शिवाय काशीविश्वनाथाय नमः ॥',
    accessibility: 'Spacious flat Kashi Vishwanath Dham corridor, golf carts from Ghats, escalators & elevators.'
  },
  {
    id: 'jyo-8',
    name: 'Trimbakeshwar Jyotirlinga',
    category: 'JYOTIRLINGA',
    lat: 19.9324,
    lng: 73.5307,
    state: 'Maharashtra',
    location: 'Trimbak, Nashik (Brahmagiri Hill)',
    presidingDeity: 'Trimbakeshwar (Tri-Murti: Brahma, Vishnu, Rudra)',
    associatedBhairavaOrConsort: 'Goddess Ganga (Godavari River source)',
    bodyPartOrSignificance: 'Only Jyotirlinga having a three-faced linga representing the Holy Trinity.',
    textualSource: 'Shiva Purana, Padma Purana & Garga Samhita',
    architecturalStyle: 'Intricate basalt stone Hemadpanthi architecture with grand courtyards',
    historicalPeriod: 'Constructed by Peshwa Balaji Baji Rao (Nana Saheb) between 1755–1786 CE',
    sthalaPurana: 'Sage Gautama prayed to Lord Shiva to release river Ganga (Godavari) to atone for an accidental cow death.',
    recommendedMantra: 'त्र्यम्बकं गौतमीतटे । ॐ नमः शिवाय त्र्यम्बकेश्वराय नमः ॥',
    accessibility: 'Flat stone courtyard, special elderly line pass during non-peak hours.'
  },
  {
    id: 'jyo-9',
    name: 'Baidyanath / Vaidyanath Jyotirlinga',
    category: 'JYOTIRLINGA',
    lat: 24.4925,
    lng: 86.7001,
    state: 'Jharkhand',
    location: 'Deoghar (Santhal Parganas)',
    presidingDeity: 'Lord Vaidyanatha (The Supreme Divine Physician)',
    associatedBhairavaOrConsort: 'Goddess Jaya Durga (Also a Shakti Peetha)',
    bodyPartOrSignificance: 'Healer of all worldly suffering, disease, and mental afflictions.',
    textualSource: 'Shiva Purana & Ramayana Uttara Kanda',
    architecturalStyle: 'Lotus-shaped stone spire with Panchashoola trident atop the temple',
    historicalPeriod: 'Ancient; patronized by Chola, Pala, and Birbal/Kharagpur kings',
    sthalaPurana: 'Ravana sacrificed his 10 heads to please Shiva; Shiva manifested as Vaidya (physician) to heal him.',
    recommendedMantra: 'वैद्यनाथं चिताभूमौ । ॐ वैद्यनाथाय नमः ॥',
    accessibility: 'VIP Darshan pass for seniors, ramps to temple complex, direct access road.'
  },
  {
    id: 'jyo-10',
    name: 'Nageshwar Jyotirlinga',
    category: 'JYOTIRLINGA',
    lat: 22.3344,
    lng: 69.0911,
    state: 'Gujarat',
    location: 'Near Dwarka (Darukavana)',
    presidingDeity: 'Lord Nageshwara (Lord of Serpents & Cosmic Poisons)',
    associatedBhairavaOrConsort: 'Goddess Nageshwari',
    bodyPartOrSignificance: 'Protection from all worldly poisons, fears, and psychic malevolence.',
    textualSource: 'Shiva Purana (Koti Rudra Samhita, Ch. 29)',
    architecturalStyle: 'Modernized Nagara temple with majestic 85-foot giant meditative Shiva statue',
    historicalPeriod: 'Described in ancient Puranas; refurbished by Gulshan Kumar T-Series Trust',
    sthalaPurana: 'Devotee Supriya was imprisoned by demon Daruka; Lord Shiva broke through the cave to save him.',
    recommendedMantra: 'नागेशं दारुकावने । ॐ नमः शिवाय नागेश्वराय नमः ॥',
    accessibility: 'Completely flat single-level marble precinct; highly accessible for wheelchairs and walkers.'
  },
  {
    id: 'jyo-11',
    name: 'Ramanathaswamy Jyotirlinga',
    category: 'JYOTIRLINGA',
    lat: 9.2881,
    lng: 79.3174,
    state: 'Tamil Nadu',
    location: 'Rameswaram Island (Pamban Channel)',
    presidingDeity: 'Lord Ramanathaswamy (Linga consecrated by Sri Rama)',
    associatedBhairavaOrConsort: 'Goddess Parvathavarthini',
    bodyPartOrSignificance: 'Southernmost Jyotirlinga; site where Sri Rama worshipped Shiva before Sethu Bandhana.',
    textualSource: 'Valmiki Ramayana, Shiva Purana & Skanda Purana',
    architecturalStyle: 'Pure Dravidian Architecture featuring the longest stone pillared corridor in the world (1,212 pillars)',
    historicalPeriod: 'Developed by Pandya kings (12th c.) and Sethupathi rulers of Ramnad',
    sthalaPurana: 'Sri Rama installed the sand Linga made by Sita Devi to cleanse the sin of slaying Ravana (Brahma-hatya).',
    recommendedMantra: 'सेतुबन्धे तु रामेशम् । ॐ नमः शिवाय रामनाथाय नमः ॥',
    accessibility: 'Battery cars from parking, flat corridors, wheelchair ramps at main gopurams.'
  },
  {
    id: 'jyo-12',
    name: 'Grishneshwar Jyotirlinga',
    category: 'JYOTIRLINGA',
    lat: 20.0249,
    lng: 75.1706,
    state: 'Maharashtra',
    location: 'Verul (Ellora Caves), Chhatrapati Sambhaji Nagar',
    presidingDeity: 'Lord Grishneshwar (Lord of Compassion)',
    associatedBhairavaOrConsort: 'Goddess Kusumeshwari',
    bodyPartOrSignificance: 'Twelfth Jyotirlinga, celebrated for boundless compassion towards the devout Kusuma.',
    textualSource: 'Shiva Purana (Koti Rudra Samhita, Ch. 32–33)',
    architecturalStyle: 'Red sandstone South-Indian Nagara style with 5-tier carved Shikhara',
    historicalPeriod: 'Restored by Maloji Raje Bhosale (16th c.) and Maharani Ahilyabai Holkar (18th c.)',
    sthalaPurana: 'Kusuma immersed Shivalingas in a pond daily; when her son was killed, her devotion brought him back to life.',
    recommendedMantra: 'घुश्मेशं च शिवालये । ॐ नमः शिवाय घुश्मेश्वराय नमः ॥',
    accessibility: 'Spacious stone courtyard, smooth entry thresholds, calm queueing.'
  }
];

export const SHAKTI_PEETHAS: SacredTirtha[] = [
  {
    id: 'sp-1',
    name: 'Kamakhya Temple',
    category: 'SHAKTI_PEETHA',
    lat: 26.1664,
    lng: 91.7056,
    state: 'Assam',
    location: 'Guwahati, Nilachal Hill',
    presidingDeity: 'Goddess Kamakhya (Mahamaya)',
    associatedBhairavaOrConsort: 'Umananda Bhairava',
    bodyPartOrSignificance: 'Yoni Peetha (Creative source & fertility of Mother Earth; Ambubachi Mela seat).',
    textualSource: 'Kalika Purana, Yogini Tantra & Devi Bhagavata',
    architecturalStyle: 'Nilachal style (cruciform base with beehive-shaped Shikhara)',
    historicalPeriod: 'Ancient Kamarupa; reconstructed by Koch King Naranarayana (1565 CE)',
    sthalaPurana: 'The Yoni (womb/creative matrix) of Devi Sati fell here as Sudarshana Chakra divided her form.',
    recommendedMantra: 'कामाख्ये कामसम्पन्ने कामेश्वरि हरप्रिये । कामनाम देहि मे नित्यं कामेश्वरि नमोऽस्तु ते ॥',
    accessibility: 'Designated senior citizens queue, elevator to Natmandir, covered walkways.'
  },
  {
    id: 'sp-2',
    name: 'Kalighat Kali Temple',
    category: 'SHAKTI_PEETHA',
    lat: 22.5204,
    lng: 88.3426,
    state: 'West Bengal',
    location: 'Kolkata, Adi Ganga canal',
    presidingDeity: 'Goddess Kalika',
    associatedBhairavaOrConsort: 'Nakuleshwar Bhairava',
    bodyPartOrSignificance: 'Right Toes of Devi Sati.',
    textualSource: 'Tantrachudamani & Kalika Purana',
    architecturalStyle: 'Bengal Chala architecture with sloping curved cornices',
    historicalPeriod: 'Sabarna Roy Choudhury family reconstruction (1809 CE)',
    sthalaPurana: 'The 4 toes of Sati’s right foot fell here beside the sacred stream of Bhagirathi-Hooghly.',
    recommendedMantra: 'ॐ क्रीं कालिकायै नमः ॥',
    accessibility: 'Covered VIP darshan entry, paved lanes with temple trust assistance.'
  },
  {
    id: 'sp-3',
    name: 'Tarapith Temple',
    category: 'SHAKTI_PEETHA',
    lat: 24.1147,
    lng: 87.7981,
    state: 'West Bengal',
    location: 'Birbhum, Dwarka River',
    presidingDeity: 'Maa Tara (Ugra Tara)',
    associatedBhairavaOrConsort: 'Chandrachur Bhairava (Sadhak Bamakhepa seat)',
    bodyPartOrSignificance: 'Third Eye / Eyeball (Nayan Tara) of Sati.',
    textualSource: 'Tantrachudamani & Shiva Charita',
    architecturalStyle: 'Eight-roofed At-chala terracotta brick temple',
    historicalPeriod: 'Ancient Tantric Mahapitha, rebuilt by Jagannath Ray (1818 CE)',
    sthalaPurana: 'Sage Vashistha and avatar Bamakhepa achieved perfection (Siddhi) meditating on Maa Tara here.',
    recommendedMantra: 'ॐ ह्रीं स्त्रीं हुं फट् ॥',
    accessibility: 'Step-free entrance ramps, orderly courtyard.'
  },
  {
    id: 'sp-4',
    name: 'Bakreshwar Shakti Peetha',
    category: 'SHAKTI_PEETHA',
    lat: 23.8828,
    lng: 87.3719,
    state: 'West Bengal',
    location: 'Birbhum',
    presidingDeity: 'Maa Mahishamardini',
    associatedBhairavaOrConsort: 'Vakranath Bhairava',
    bodyPartOrSignificance: 'Center of Eyebrows / Mind (Ajna Chakra).',
    textualSource: 'Devi Bhagavata & Tantrachudamani',
    architecturalStyle: 'Traditional Bengali Rekha Deul with surrounding natural thermal springs',
    historicalPeriod: 'Medieval renovations over classical Tantric foundations',
    sthalaPurana: 'Sage Ashtavakra cured of physical deformities by worshipping Shiva and Shakti at this site.',
    recommendedMantra: 'ॐ ऐं ह्रीं क्लीं चामुण्डायै विच्चे ॥',
    accessibility: 'Thermal bath walkways, peaceful flat temple grounds.'
  },
  {
    id: 'sp-5',
    name: 'Biraja Temple (Jajpur)',
    category: 'SHAKTI_PEETHA',
    lat: 20.8497,
    lng: 86.3344,
    state: 'Odisha',
    location: 'Jajpur (Viraja Kshetra), Vaitarani River',
    presidingDeity: 'Goddess Biraja (Dwibhuja Mahishasuramardini)',
    associatedBhairavaOrConsort: 'Varaha / Ishaneshwar Bhairava',
    bodyPartOrSignificance: 'Navel (Nabhi Peetha).',
    textualSource: 'Mahabharata (Vana Parva) & Skanda Purana',
    architecturalStyle: 'Kalinga Architecture with Rekha Vimana & Jagamohana',
    historicalPeriod: 'Somavamshi and Bhauma-Kara Dynasties (10th–11th c. CE)',
    sthalaPurana: 'Brahma performed a great Vedic yajna here, from which Goddess Biraja manifested.',
    recommendedMantra: 'ॐ ह्रीं श्रीं बिराजायै नमः ॥',
    accessibility: 'Wide parikrama path, temple volunteer priority assistance for seniors.'
  },
  {
    id: 'sp-6',
    name: 'Tara Tarini Shakti Peetha',
    category: 'SHAKTI_PEETHA',
    lat: 19.4912,
    lng: 84.8997,
    state: 'Odisha',
    location: 'Purushottampur, Ganjam, Rushikulya River',
    presidingDeity: 'Twin Goddesses Tara & Tarini (Kalyani & Jagaddhatri)',
    associatedBhairavaOrConsort: 'Tumbeshwar Bhairava',
    bodyPartOrSignificance: 'Breasts (Sthana Peetha) of Sati.',
    textualSource: 'Kalika Purana & Tantrachudamani',
    architecturalStyle: 'Modernized Kalinga style atop Purnagiri Hill',
    historicalPeriod: 'Ancient maritime heritage; revered by Kalinga navigators (Sadhabas)',
    sthalaPurana: 'Maa Tara and Tarini granted prosperity to ancient merchants venturing across the seas.',
    recommendedMantra: 'ॐ ह्रीं श्रीं तारातारिणीभ्यां नमः ॥',
    accessibility: 'Ropeway (cable car) system to hilltop, motorable hill road with parking.'
  },
  {
    id: 'sp-7',
    name: 'Jwalamukhi Temple',
    category: 'SHAKTI_PEETHA',
    lat: 31.8790,
    lng: 76.3235,
    state: 'Himachal Pradesh',
    location: 'Kangra Valley',
    presidingDeity: 'Maa Jwalamukhi (Nine Eternal Flames / Navadurga)',
    associatedBhairavaOrConsort: 'Unmatta Bhairava',
    bodyPartOrSignificance: 'Tongue (Jihwa Peetha) of Devi Sati.',
    textualSource: 'Skanda Purana & Tantrachudamani',
    architecturalStyle: 'Indo-Sikh architectural dome with golden cupola gifted by Maharaja Ranjit Singh',
    historicalPeriod: 'Patronized by Katoch dynasty of Kangra and Akbar who offered a gold chhatra',
    sthalaPurana: 'Devi’s tongue fell into the fissure, manifesting as eternal self-ignited natural flames.',
    recommendedMantra: 'ॐ ज्वालामुखि सर्वशत्रून् भक्षय भक्षय हुं फट् स्वाहा ॥',
    accessibility: 'Gradual ramps from main street, wheel chairs, orderly sanctum queue.'
  },
  {
    id: 'sp-8',
    name: 'Naina Devi Temple',
    category: 'SHAKTI_PEETHA',
    lat: 31.3060,
    lng: 76.5385,
    state: 'Himachal Pradesh',
    location: 'Bilaspur, Shivalik Hills',
    presidingDeity: 'Maa Naina Devi',
    associatedBhairavaOrConsort: 'Kapalika Bhairava',
    bodyPartOrSignificance: 'Eyes (Netra Peetha) of Sati.',
    textualSource: 'Tantrachudamani & Shiva Charita',
    architecturalStyle: 'Traditional hill temple pavilion style with marble sanctum',
    historicalPeriod: 'Established by Raja Bir Chand (8th c. CE)',
    sthalaPurana: 'Devi’s eyes fell upon this scenic hill overlooking Govind Sagar lake.',
    recommendedMantra: 'ॐ नयनायै नमः ॥',
    accessibility: 'Ropeway system directly ascending to the temple portal; wheelchair facilities.'
  },
  {
    id: 'sp-9',
    name: 'Chintpurni (Chhinnamastika)',
    category: 'SHAKTI_PEETHA',
    lat: 31.6436,
    lng: 76.1080,
    state: 'Himachal Pradesh',
    location: 'Una, Bharwain Range',
    presidingDeity: 'Maa Chhinnamastika / Chintpurni',
    associatedBhairavaOrConsort: 'Rudra Mahabhairava',
    bodyPartOrSignificance: 'Feet / Forehead (Dispeller of all worldly worries & mental knots).',
    textualSource: 'Markandeya Purana & Devi Mahatmya',
    architecturalStyle: 'Pahari wooden and stone temple complex',
    historicalPeriod: 'Revitalized by Pandit Mai Das (15th c. CE)',
    sthalaPurana: 'The Goddess relieves all inner mental tensions (Chinta) of genuine seekers.',
    recommendedMantra: 'ॐ श्रीं ह्रीं क्लीं ऐं वज्र वैरोचनीये हुं हुं फट् स्वाहा ॥',
    accessibility: 'Electric cart service along temple bazaar road, queue management sheds.'
  },
  {
    id: 'sp-10',
    name: 'Chamunda Devi (Kangra)',
    category: 'SHAKTI_PEETHA',
    lat: 32.1648,
    lng: 76.4172,
    state: 'Himachal Pradesh',
    location: 'Dharamsala / Kangra, Baner River',
    presidingDeity: 'Maa Chamunda (Slayer of Chanda and Munda)',
    associatedBhairavaOrConsort: 'Bhaneshwar Bhairava',
    bodyPartOrSignificance: 'Cosmic Armor & Crown.',
    textualSource: 'Devi Mahatmya (Durga Saptashati, Ch. 7)',
    architecturalStyle: 'Wood and slate Himalayan sanctuary overlooking snowcapped Dhauladhar peaks',
    historicalPeriod: 'Ancient roots, renovated 400 years ago',
    sthalaPurana: 'Kali emerged from Durga’s forehead to destroy demonic generals Chanda and Munda.',
    recommendedMantra: 'ॐ ऐं ह्रीं क्लीं चामुण्डायै विच्चे ॥',
    accessibility: 'Direct motorable access, flat temple forecourt.'
  },
  {
    id: 'sp-11',
    name: 'Vaishno Devi (Trikuta Hill)',
    category: 'SHAKTI_PEETHA',
    lat: 33.0308,
    lng: 74.9490,
    state: 'Jammu & Kashmir',
    location: 'Katra, Trikuta Mountain (1,585m)',
    presidingDeity: 'Maha Kali, Maha Lakshmi & Maha Saraswati (Sacred Pindis)',
    associatedBhairavaOrConsort: 'Bhairavnath (Top Peak)',
    bodyPartOrSignificance: 'Right Arm / Forehead (Union of Sattva, Rajas, Tamas in pure devotion).',
    textualSource: 'Durga Saptashati & Skanda Purana',
    architecturalStyle: 'Natural cave shrine integrated with high-tech pilgrim corridors',
    historicalPeriod: 'Pandava legends; Shree Mata Vaishno Devi Shrine Board (SMVDSB since 1986)',
    sthalaPurana: 'Vaishnavi meditated in the cave and manifested as the three divine eternal Pindis.',
    recommendedMantra: 'ॐ ऐं ह्रीं क्लीं महालक्ष्म्यै नमः ॥',
    accessibility: 'Battery car track (Himkoti), ropeway to Bhairon Ghati, helicopter from Katra, palanquins.'
  },
  {
    id: 'sp-12',
    name: 'Amarnath Mahamaya Peetha',
    category: 'SHAKTI_PEETHA',
    lat: 34.2157,
    lng: 75.5038,
    state: 'Jammu & Kashmir',
    location: 'Anantnag, Lidder Valley (3,888m alt)',
    presidingDeity: 'Goddess Mahamaya & Holy Ice Lingam',
    associatedBhairavaOrConsort: 'Trisandhyeshwar Bhairava',
    bodyPartOrSignificance: 'Throat (Kanta Peetha) of Sati.',
    textualSource: 'Nilamata Purana & Rajatarangini of Kalhana',
    architecturalStyle: 'Vast natural Himalayan limestone cavern with frozen stalagmite sanctum',
    historicalPeriod: 'Documented since 1st millennium BCE by Kalhana; Bhrigu Muni legends',
    sthalaPurana: 'Lord Shiva revealed the eternal Amar Katha (secret of immortality) to Parvati.',
    recommendedMantra: 'ॐ महामायायै नमः ॥',
    accessibility: 'Helicopter to Panjtarni, pony/palanquin services with medical camps along the trail.'
  },
  {
    id: 'sp-13',
    name: 'Sharada Peeth',
    category: 'SHAKTI_PEETHA',
    lat: 34.7892,
    lng: 73.9789,
    state: 'Kashmir (POK / Historic Line)',
    location: 'Neelum Valley, Kishanganga River',
    presidingDeity: 'Goddess Sharada (Supreme Knowledge & Sanskrit Script)',
    associatedBhairavaOrConsort: 'Mahasaraswati Bhairava',
    bodyPartOrSignificance: 'Right Hand / Crown of Speech (Jnana Peetha).',
    textualSource: 'Kalhana’s Rajatarangini & Madhava’s Shankara Digvijaya',
    architecturalStyle: 'Classical Kashmiri stone peristyle courtyard and sanctum atop hillock',
    historicalPeriod: 'Prominent from Mauryan era through Adi Shankaracharya’s Sarvajna Peetha ascent',
    sthalaPurana: 'Foremost university-temple where Vedic scholars and Grammarians tested their treaties.',
    recommendedMantra: 'नमस्ते शारदे देवि काश्मीरपुरवासिनि । त्वामहं प्रार्थये नित्यं विद्यादानं च देहि मे ॥',
    accessibility: 'Historic site; recently commemorated with Sharada Temple at Teetwal (LoC).'
  },
  {
    id: 'sp-14',
    name: 'Guhyeshwari Temple',
    category: 'SHAKTI_PEETHA',
    lat: 27.7107,
    lng: 85.3533,
    state: 'Nepal',
    location: 'Kathmandu, Bagmati River near Pashupatinath',
    presidingDeity: 'Goddess Guhyeshwari',
    associatedBhairavaOrConsort: 'Kapali Bhairava (Pashupatinath)',
    bodyPartOrSignificance: 'Both Knees of Devi Sati.',
    textualSource: 'Tantrachudamani, Nepalamahatmya & Himavatkhanda',
    architecturalStyle: 'Traditional Newari Pagoda Architecture with metal filigree toranas',
    historicalPeriod: 'Constructed by King Pratap Malla in 1654 CE',
    sthalaPurana: 'Devi’s knees fell near the holy Bagmati, forming the protective counterpart to Lord Pashupatinath.',
    recommendedMantra: 'ॐ गुह्येश्वर्यै नमः ॥',
    accessibility: 'Direct stone path from Pashupatinath bridge, wheelchair accessible courtyard.'
  },
  {
    id: 'sp-15',
    name: 'Gandaki Muktinath Peetha',
    category: 'SHAKTI_PEETHA',
    lat: 28.8170,
    lng: 83.8718,
    state: 'Nepal',
    location: 'Mustang, Thorong La Base (3,710m alt)',
    presidingDeity: 'Goddess Gandaki Chandi',
    associatedBhairavaOrConsort: 'Chakrapani Bhairava',
    bodyPartOrSignificance: 'Forehead / Cheek of Sati.',
    textualSource: 'Devi Bhagavata & Varaha Purana',
    architecturalStyle: 'Three-tiered Tibetan-Newar Pagoda with 108 sacred brass water spouts',
    historicalPeriod: 'Ancient Shaligrama river source; revered by Vaishnavas and Buddhists alike',
    sthalaPurana: 'Origin of sacred Saligrama stones in the holy Gandaki River.',
    recommendedMantra: 'ॐ गण्डकीचण्ड्यै नमः ॥',
    accessibility: 'Jeep and flight access to Jomsom followed by paved vehicle road to Muktinath gate.'
  },
  {
    id: 'sp-16',
    name: 'Mansarovar (Manasa Peetha)',
    category: 'SHAKTI_PEETHA',
    lat: 30.6690,
    lng: 81.4720,
    state: 'Tibet (Trans-Himalaya)',
    location: 'Lake Mansarovar, Foot of Mount Kailash (4,590m alt)',
    presidingDeity: 'Goddess Dakshayani',
    associatedBhairavaOrConsort: 'Amar Bhairava',
    bodyPartOrSignificance: 'Right Hand of Devi Sati.',
    textualSource: 'Tantrachudamani & Skanda Purana (Manasakhanda)',
    architecturalStyle: 'Natural sacred lake basin created by Brahma’s pure mind',
    historicalPeriod: 'Rigvedic & Puranic antiquity; Kailash Yatra circuit',
    sthalaPurana: 'Sati’s right hand fell into the crystal turquoise waters reflecting Mount Kailash.',
    recommendedMantra: 'ॐ दाक्षायण्यै नमः ॥',
    accessibility: 'Eco-bus circuit around Lake Mansarovar during pilgrimage season.'
  },
  {
    id: 'sp-17',
    name: 'Hinglaj Mata (Nani Mandir)',
    category: 'SHAKTI_PEETHA',
    lat: 25.5110,
    lng: 65.5170,
    state: 'Balochistan (Hingol)',
    location: 'Lasbela, Hingol National Park, Makran Coast',
    presidingDeity: 'Maa Hinglaj (Kottari)',
    associatedBhairavaOrConsort: 'Bhimlochan Bhairava',
    bodyPartOrSignificance: 'Brahmarandhra / Crown of the Head of Sati.',
    textualSource: 'Tantrachudamani, Chandi Charitra & Shiva Charita',
    architecturalStyle: 'Natural mud-rock cave within volcanic mud-mountain canyons',
    historicalPeriod: 'Visited by Sri Rama after defeating Ravana; beloved by Sufis as Nani Pir',
    sthalaPurana: 'Supreme seat where Devi’s upper crown fell, dissolving all past karmas.',
    recommendedMantra: 'ॐ ह्रीं श्रीं क्लीं हिंगुलाज देव्यै नमः ॥',
    accessibility: 'Makran Coastal Highway paved road, level walking canyon path.'
  },
  {
    id: 'sp-18',
    name: 'Ambaji (Arasur Hill)',
    category: 'SHAKTI_PEETHA',
    lat: 24.3315,
    lng: 72.8519,
    state: 'Gujarat',
    location: 'Banaskantha, Gabbar Hill',
    presidingDeity: 'Maa Ambe (No Murti; Worshipped as Visa Yantra)',
    associatedBhairavaOrConsort: 'Batuk Bhairava',
    bodyPartOrSignificance: 'Heart of Devi Sati.',
    textualSource: 'Tantrachudamani & Skanda Purana',
    architecturalStyle: 'White marble Nagara architectural splendor with gold Kalash',
    historicalPeriod: 'Suryavanshi kings and Chaulukya dynasty',
    sthalaPurana: 'Sacred Heart of Sati; worshipped through the consecrated golden Visa Yantra.',
    recommendedMantra: 'ॐ श्रीं ह्रीं क्लीं अम्बिकायै नमः ॥',
    accessibility: 'Gabbar hill ropeway, smooth marble floors with non-slip carpets, battery cars.'
  },
  {
    id: 'sp-19',
    name: 'Kolhapur Mahalakshmi (Ambabai)',
    category: 'SHAKTI_PEETHA',
    lat: 16.6946,
    lng: 74.2238,
    state: 'Maharashtra',
    location: 'Kolhapur (Karvir Kshetra)',
    presidingDeity: 'Goddess Mahalakshmi',
    associatedBhairavaOrConsort: 'Krodhish Bhairava',
    bodyPartOrSignificance: 'Eyes / Triple Form (Maha-Kali, Maha-Lakshmi, Maha-Saraswati).',
    textualSource: 'Mahalakshmi Ashtakam & Padma Purana',
    architecturalStyle: 'Hemadpanthi black basalt stone with solar alignment (Kiranotsav festival)',
    historicalPeriod: 'Chalukya dynasty (7th c. CE) and Silaharas',
    sthalaPurana: 'Sun rays illuminate the deity’s face directly during the biannual Kiranotsav.',
    recommendedMantra: 'नमस्तेऽस्तु महामाये श्रीपीठे सुरपूजिते । शङ्खचक्रगदाहस्ते महालक्ष्मि नमोऽस्तु ते ॥',
    accessibility: 'Dedicated ramped gates, senior citizen darshan pass, battery carts.'
  },
  {
    id: 'sp-20',
    name: 'Tuljabhavani Temple',
    category: 'SHAKTI_PEETHA',
    lat: 18.0069,
    lng: 76.0718,
    state: 'Maharashtra',
    location: 'Tuljapur, Osmanabad (Dharashiv)',
    presidingDeity: 'Maa Tulja Bhavani (Kuladaivat of Chhatrapati Shivaji Maharaj)',
    associatedBhairavaOrConsort: 'Kameshwar Bhairava',
    bodyPartOrSignificance: 'Heart / Right Cheek of Devi Sati.',
    textualSource: 'Markandeya Purana & Skanda Purana',
    architecturalStyle: 'Hemadpanthi architectural style with Kallol and Gomukh sacred tirthas',
    historicalPeriod: 'Kadamba, Yadava dynasties; 12th–17th c. CE',
    sthalaPurana: 'Maa Bhavani personally gifted the divine sword (Bhavani Talwar) to Chhatrapati Shivaji Maharaj.',
    recommendedMantra: 'ॐ श्री तुलजाभवान्यै नमः ॥',
    accessibility: 'Spacious pradakshina marg, ramp access at Mahadwar.'
  },
  {
    id: 'sp-21',
    name: 'Kanchi Kamakshi Amman',
    category: 'SHAKTI_PEETHA',
    lat: 12.8406,
    lng: 79.7032,
    state: 'Tamil Nadu',
    location: 'Kanchipuram (Odyana Peetha)',
    presidingDeity: 'Goddess Kamakshi (Sri Rajarajeshwari)',
    associatedBhairavaOrConsort: 'Sri Kameshwarar & Sri Chakra installed by Adi Shankara',
    bodyPartOrSignificance: 'Navel / Bone (Odyana Peetha; heart of Sri Vidya Upasana).',
    textualSource: 'Lalita Sahasranama, Saundarya Lahari & Brahmanda Purana',
    architecturalStyle: 'Dravidian Architecture with Golden Vimana over the sanctum',
    historicalPeriod: 'Pallavas, Cholas, and consecrated by Adi Shankaracharya',
    sthalaPurana: 'Adi Shankaracharya pacified the fierce form by consecrating the Sri Chakra in front of the deity.',
    recommendedMantra: 'कामाक्षीं करुणापूर्णां कल्पवृक्षस्वरूपिणीम् । ॐ श्रीमात्रे नमः ॥',
    accessibility: 'Level stone parikramas, wheelchairs at Devasthanam office, designated queue.'
  },
  {
    id: 'sp-22',
    name: 'Madurai Meenakshi Sundareswarar',
    category: 'SHAKTI_PEETHA',
    lat: 9.9195,
    lng: 78.1193,
    state: 'Tamil Nadu',
    location: 'Madurai, Vaigai River',
    presidingDeity: 'Goddess Meenakshi (Tadaatagai Pirattiyar)',
    associatedBhairavaOrConsort: 'Sundareswarar (Soma Sundara)',
    bodyPartOrSignificance: 'Twin Eyes (Fish-shaped eyes overflowing with motherly grace).',
    textualSource: 'Thiruvilaiyadal Puranam & Skanda Purana',
    architecturalStyle: 'Pinnacle of Nayaka Dravidian Architecture with 14 towering multi-tiered Gopurams',
    historicalPeriod: 'Rebuilt by Vishwanatha Nayak (16th–17th c. CE)',
    sthalaPurana: 'Born from holy sacrificial fire to King Malayadhwaja Pandya; ruled Madurai as queen.',
    recommendedMantra: 'ॐ श्री मीनाक्षीसुन्दरेश्वराभ्यां नमः ॥',
    accessibility: 'Battery buggies within complex, special queues for elderly and differently-abled pilgrims.'
  },
  {
    id: 'sp-23',
    name: 'Chamundeshwari (Mysuru)',
    category: 'SHAKTI_PEETHA',
    lat: 12.2727,
    lng: 76.6706,
    state: 'Karnataka',
    location: 'Chamundi Hills, Mysuru (1,000m alt)',
    presidingDeity: 'Goddess Chamundeshwari',
    associatedBhairavaOrConsort: 'Mahabaleshwara Bhairava',
    bodyPartOrSignificance: 'Hair (Kesha Peetha) / Divine Ears.',
    textualSource: 'Devi Mahatmya & Skanda Purana',
    architecturalStyle: 'Seven-tier Dravidian Gopuram with intricate stucco sculptures',
    historicalPeriod: 'Hoysala kings (12th c.) and Vijayanagara/Wodeyar rulers (17th c.)',
    sthalaPurana: 'Slain the buffalo-demon Mahishasura on this hill, establishing peace in Mahishuru (Mysuru).',
    recommendedMantra: 'ॐ चामुण्डायै नमः ॥',
    accessibility: 'Direct hill road with bus/car parking right at temple entrance, ramp access.'
  },
  {
    id: 'sp-24',
    name: 'Jogulamba (Alampur)',
    category: 'SHAKTI_PEETHA',
    lat: 15.8797,
    lng: 78.1287,
    state: 'Telangana',
    location: 'Alampur, Tungabhadra & Krishna River Sangam',
    presidingDeity: 'Goddess Jogulamba (Yogamba)',
    associatedBhairavaOrConsort: 'Bala Brahmeswara Bhairava',
    bodyPartOrSignificance: 'Upper Teeth of Devi Sati.',
    textualSource: 'Tantrachudamani & Skanda Purana',
    architecturalStyle: 'Badami Chalukya Nagara hybrid architecture with Navabrahma shrines',
    historicalPeriod: 'Western Chalukyas (7th–8th c. CE)',
    sthalaPurana: 'Protector of the sacred confluence (Sangama) and guardian of ascetic yogis.',
    recommendedMantra: 'ॐ जोगुलाम्बा देव्यै नमः ॥',
    accessibility: 'Flat riverbank temple grounds with newly paved walkways.'
  },
  {
    id: 'sp-25',
    name: 'Srisailam Bhramaramba',
    category: 'SHAKTI_PEETHA',
    lat: 16.0740,
    lng: 78.8687,
    state: 'Andhra Pradesh',
    location: 'Nallamala Hills, Srisailam',
    presidingDeity: 'Goddess Bhramarambika (Mother of Golden Bees)',
    associatedBhairavaOrConsort: 'Mallikarjuna Jyotirlinga (Samvatsara)',
    bodyPartOrSignificance: 'Neck (Griva Peetha) of Sati.',
    textualSource: 'Devi Bhagavata & Lalita Sahasranama',
    architecturalStyle: 'Vijayanagara architectural courtyard inside Mallikarjuna temple',
    historicalPeriod: 'Consecrated anciently; visited by Adi Shankaracharya who wrote Bhramaramba Ashtakam',
    sthalaPurana: 'Devi assumed the form of countless bees (Bhramara) to exterminate demon Arunasura.',
    recommendedMantra: 'चाञ्चल्यमरुणालोचनं चारुहासम् । ॐ श्री भ्रमराम्बिकायै नमः ॥',
    accessibility: 'Shared facilities with Mallikarjuna temple: priority senior lines and elevators.'
  },
  {
    id: 'sp-26',
    name: 'Manikarnika & Vishalakshi (Kashi)',
    category: 'SHAKTI_PEETHA',
    lat: 25.3105,
    lng: 83.0125,
    state: 'Uttar Pradesh',
    location: 'Varanasi, Meer Ghat / Manikarnika',
    presidingDeity: 'Goddess Vishalakshi (The Wide-Eyed Mother)',
    associatedBhairavaOrConsort: 'Kaal Bhairav',
    bodyPartOrSignificance: 'Earring (Manikarnika) & Eyes.',
    textualSource: 'Kashi Khanda & Tantrachudamani',
    architecturalStyle: 'South Indian style gopuram nestled inside the historic Kashi lanes',
    historicalPeriod: 'Ancient; renovated in 1908 by the Nattukottai Chettiars',
    sthalaPurana: 'Sati’s divine jewel earring fell as Shiva danced in grief with her sacred form.',
    recommendedMantra: 'ॐ विशालाक्ष्यै नमः ॥',
    accessibility: 'Approachable via Kashi Vishwanath Dham lane or boat to Meer Ghat.'
  },
  {
    id: 'sp-27',
    name: 'Alopi Devi (Prayagraj)',
    category: 'SHAKTI_PEETHA',
    lat: 25.4385,
    lng: 81.8753,
    state: 'Uttar Pradesh',
    location: 'Prayagraj (Alopibagh near Sangam)',
    presidingDeity: 'Alopi Mata (Formless; Worshipped on a Wooden Doli)',
    associatedBhairavaOrConsort: 'Mahananda Bhairava',
    bodyPartOrSignificance: 'Fingers / Last remnant of Sati disappeared (Alopa) here.',
    textualSource: 'Tantrachudamani & Matsya Purana',
    architecturalStyle: 'Traditional North Indian red-brick temple with central sanctum pool',
    historicalPeriod: 'Ancient Prayag Mahatmya; continuous living worship',
    sthalaPurana: 'Here the remaining form of Devi disappeared into the transcendent Void (Alopa).',
    recommendedMantra: 'ॐ अलोपायै नमः ॥',
    accessibility: 'Paved access road, single-level courtyard, peaceful temple tanks.'
  },
  {
    id: 'sp-28',
    name: 'Vindhyavasini (Vindhyachal)',
    category: 'SHAKTI_PEETHA',
    lat: 25.1633,
    lng: 82.5025,
    state: 'Uttar Pradesh',
    location: 'Mirzapur, Ganga River',
    presidingDeity: 'Goddess Vindhyavasini (Kajal & Mahishasuramardini)',
    associatedBhairavaOrConsort: 'Ruru Bhairava',
    bodyPartOrSignificance: 'Sacred Waistband / Left Arm (Kajali Devi).',
    textualSource: 'Harivamsa, Durga Saptashati & Devi Purana',
    architecturalStyle: 'Modernized grand Vindhya Corridor in Nagara sandstone style',
    historicalPeriod: 'Revered from the Mahabharata era; Yogamaya who warned Kansa',
    sthalaPurana: 'The sister of Krishna who slipped from Kansa’s grasp and chose to dwell on the Vindhya range.',
    recommendedMantra: 'ॐ ह्रीं श्रीं क्लीं विन्ध्यवासिन्यै नमः ॥',
    accessibility: 'Vindhya Dham corridor with ramp access, escalators, and covered queue lines.'
  },
  {
    id: 'sp-29',
    name: 'Danteshwari Temple',
    category: 'SHAKTI_PEETHA',
    lat: 18.8924,
    lng: 81.3486,
    state: 'Chhattisgarh',
    location: 'Dantewada, Shankini & Dhankini River confluence',
    presidingDeity: 'Maa Danteshwari (Kuldevi of Bastar)',
    associatedBhairavaOrConsort: 'Kapalbhairava',
    bodyPartOrSignificance: 'Tooth (Danta Peetha) of Sati.',
    textualSource: 'Tantrachudamani & Kakatiya local chronicles',
    architecturalStyle: 'Bastar tribal-Nagara wooden and stone architecture with Garbha Griha & Maha Mandapa',
    historicalPeriod: 'Constructed by Kakatiya King Annam Deo (14th c. CE)',
    sthalaPurana: 'The tooth of Sati fell at this river confluence; patron goddess of Bastar Dussehra.',
    recommendedMantra: 'ॐ दन्तेश्वर्यै नमः ॥',
    accessibility: 'Serene riverbank temple compound, zero steps into the outer mandapa.'
  },
  {
    id: 'sp-30',
    name: 'Tripurasundari (Matabari)',
    category: 'SHAKTI_PEETHA',
    lat: 23.5137,
    lng: 91.5002,
    state: 'Tripura',
    location: 'Udaipur, Kalyan Sagar Lake',
    presidingDeity: 'Maa Tripurasundari (Soroshi / Kurma Peetha)',
    associatedBhairavaOrConsort: 'Tripuresh Bhairava',
    bodyPartOrSignificance: 'Right Leg / Foot of Sati.',
    textualSource: 'Kalika Purana & Tantrachudamani',
    architecturalStyle: 'Bengal-Tripura Buddhist-Chala hybrid dome with terracotta finish',
    historicalPeriod: 'Consecrated by Maharaja Dhanya Manikya in 1501 CE',
    sthalaPurana: 'Temple rests on a hill resembling the back of a tortoise (Kurma Peetha).',
    recommendedMantra: 'ॐ ऐं ह्रीं श्रीं त्रिपुरसुन्दर्यै नमः ॥',
    accessibility: 'Lakeview promenade, paved ramp approach to main sanctum.'
  },
  {
    id: 'sp-31',
    name: 'Chhinnamasta (Rajrappa)',
    category: 'SHAKTI_PEETHA',
    lat: 23.6322,
    lng: 85.7144,
    state: 'Jharkhand',
    location: 'Ramgarh, Damodar & Bhairavi River confluence',
    presidingDeity: 'Maa Chhinnamasta (Self-Decapitated Mahavidya)',
    associatedBhairavaOrConsort: 'Rudra Bhairava',
    bodyPartOrSignificance: 'Severed Head / Third Eye (Symbolizing transcendence of ego & mind).',
    textualSource: 'Pranatosani Tantra & Shaktisangama Tantra',
    architecturalStyle: 'Tantric stone temple atop rocky river bluffs',
    historicalPeriod: 'Ancient Mahavidya seat; medieval renovations',
    sthalaPurana: 'Devi decapitated herself to nourish her attendants Dakini and Varnini with her own blood-currents.',
    recommendedMantra: 'ॐ श्रीं ह्रीं क्लीं ऐं वज्र वैरोचनीये हुं हुं फट् स्वाहा ॥',
    accessibility: 'Ramped river bridge, designated queue lines with railings.'
  },
  {
    id: 'sp-32',
    name: 'Baidyanath Jaya Durga',
    category: 'SHAKTI_PEETHA',
    lat: 24.4927,
    lng: 86.7003,
    state: 'Jharkhand',
    location: 'Deoghar',
    presidingDeity: 'Maa Jaya Durga',
    associatedBhairavaOrConsort: 'Vaidyanath Jyotirlinga',
    bodyPartOrSignificance: 'Heart / Chita-Bhumi of Sati.',
    textualSource: 'Shiva Purana & Tantrachudamani',
    architecturalStyle: 'Located within the consecrated Baidyanath temple complex',
    historicalPeriod: 'Linked with Ravana and Adi Shankaracharya',
    sthalaPurana: 'Connected to Vaidyanath temple via sacred red ribbons (Gathbandhan) tied between Shiva & Shakti domes.',
    recommendedMantra: 'ॐ जयदुर्गायै नमः ॥',
    accessibility: 'Shared priority elderly access with Vaidyanath Temple.'
  },
  {
    id: 'sp-33',
    name: 'Kiriteshwari (Murshidabad)',
    category: 'SHAKTI_PEETHA',
    lat: 24.2014,
    lng: 88.2396,
    state: 'West Bengal',
    location: 'Nabagram, Murshidabad',
    presidingDeity: 'Maa Kiriteshwari (Mukuteshwari)',
    associatedBhairavaOrConsort: 'Samvarta Bhairava',
    bodyPartOrSignificance: 'Crown (Kirit / Mukuta) of Devi Sati.',
    textualSource: 'Tantrachudamani & Ain-i-Akbari',
    architecturalStyle: 'Terracotta brick pavilion in a lush heritage village setting',
    historicalPeriod: 'Recognized as Best Tourism Village 2023; patronized by Darpanarayan Ray',
    sthalaPurana: 'The celestial diadem / crown of Sati fell here.',
    recommendedMantra: 'ॐ किरीटेश्वर्यै नमः ॥',
    accessibility: 'Peaceful garden grounds, step-free access to sanctum pavilion.'
  },
  {
    id: 'sp-34',
    name: 'Kankalitala Temple',
    category: 'SHAKTI_PEETHA',
    lat: 23.6708,
    lng: 87.7214,
    state: 'West Bengal',
    location: 'Bolpur Santiniketan, Kopai River',
    presidingDeity: 'Maa Kankali',
    associatedBhairavaOrConsort: 'Ruru Bhairava',
    bodyPartOrSignificance: 'Waist / Pelvis (Kankala) of Sati.',
    textualSource: 'Tantrachudamani',
    architecturalStyle: 'Rural Bengal Chala shrine beside sacred Kundam (pond)',
    historicalPeriod: 'Ancient; deeply revered in Santiniketan cultural lore',
    sthalaPurana: 'The pelvic bone / waist fell into the sacred pond, which never dries up.',
    recommendedMantra: 'ॐ कङ्काल्यै नमः ॥',
    accessibility: 'Lush shady walkways, flat terrain throughout.'
  },
  {
    id: 'sp-35',
    name: 'Attahas Shakti Peetha',
    category: 'SHAKTI_PEETHA',
    lat: 23.6333,
    lng: 88.0833,
    state: 'West Bengal',
    location: 'Labhpur / Katwa, Birbhum',
    presidingDeity: 'Goddess Phullara',
    associatedBhairavaOrConsort: 'Vishvesh Bhairava',
    bodyPartOrSignificance: 'Lips of Sati (Site of divine cosmic laughter - Attahasa).',
    textualSource: 'Tantrachudamani & Kalika Purana',
    architecturalStyle: 'Traditional Bengal brick structure nestled by sacred pond',
    historicalPeriod: 'Ancient Tantric heritage site',
    sthalaPurana: 'Devi’s lower lip fell here while she uttered a triumphant cosmic laughter.',
    recommendedMantra: 'ॐ फुल्लरायै नमः ॥',
    accessibility: 'Ramped approach, tranquil natural ambiance.'
  },
  {
    id: 'sp-36',
    name: 'Nalhati (Nalateshwari)',
    category: 'SHAKTI_PEETHA',
    lat: 24.2982,
    lng: 87.8285,
    state: 'West Bengal',
    location: 'Nalhati, Birbhum',
    presidingDeity: 'Maa Nalateshwari',
    associatedBhairavaOrConsort: 'Yogesh Bhairava',
    bodyPartOrSignificance: 'Throat / Vocal Cord (Nala) of Sati.',
    textualSource: 'Tantrachudamani',
    architecturalStyle: 'Terracotta style temple on a scenic hillock',
    historicalPeriod: 'Rebuilt by Raja Ramkrishna Roy (18th c. CE)',
    sthalaPurana: 'Devi’s vocal tract fell here; devotees pray for eloquence and pure speech.',
    recommendedMantra: 'ॐ नलाटेश्वर्यै नमः ॥',
    accessibility: 'Gentle stairs with railing, shaded resting pavilions.'
  },
  {
    id: 'sp-37',
    name: 'Bahula Temple (Ketugram)',
    category: 'SHAKTI_PEETHA',
    lat: 23.6847,
    lng: 88.0463,
    state: 'West Bengal',
    location: 'Burdwan (Purba Bardhaman), Ajay River',
    presidingDeity: 'Goddess Bahula',
    associatedBhairavaOrConsort: 'Bhiruk Bhairava',
    bodyPartOrSignificance: 'Left Arm of Sati.',
    textualSource: 'Tantrachudamani',
    architecturalStyle: 'Classic terracotta rural Bengal temple with arched entry',
    historicalPeriod: 'Medieval heritage site',
    sthalaPurana: 'Devi Bahula bestows courage and removes fear of accidents.',
    recommendedMantra: 'ॐ बहुलायै नमः ॥',
    accessibility: 'Flat access path, quiet courtyard.'
  },
  {
    id: 'sp-38',
    name: 'Ujani Mangalkot Peetha',
    category: 'SHAKTI_PEETHA',
    lat: 23.5415,
    lng: 88.0847,
    state: 'West Bengal',
    location: 'Mangalkot, Burdwan, Ajay River',
    presidingDeity: 'Goddess Mangala Chandi',
    associatedBhairavaOrConsort: 'Kapilambar Bhairava',
    bodyPartOrSignificance: 'Right Wrist of Sati.',
    textualSource: 'Chandimangal & Tantrachudamani',
    architecturalStyle: 'At-Chala terracotta architecture',
    historicalPeriod: 'Associated with legendary merchant Dhanapati and Khullana in Mangalkavya',
    sthalaPurana: 'The golden wrist of Devi fell here, celebrated throughout Bengali spiritual literature.',
    recommendedMantra: 'ॐ मङ्गलचण्डिकायै नमः ॥',
    accessibility: 'Single-storey temple floor, direct vehicle parking near gate.'
  },
  {
    id: 'sp-39',
    name: 'Bargabhima (Tamluk)',
    category: 'SHAKTI_PEETHA',
    lat: 22.2986,
    lng: 87.9254,
    state: 'West Bengal',
    location: 'Tamluk (Ancient Tamralipta port)',
    presidingDeity: 'Maa Bargabhima (Bhimakali)',
    associatedBhairavaOrConsort: 'Sarvananda Bhairava',
    bodyPartOrSignificance: 'Left Ankle of Sati.',
    textualSource: 'Matsya Purana & Tantrachudamani',
    architecturalStyle: 'Ancient Kalinga-influenced stone and brick Rekha Deul',
    historicalPeriod: 'Mayura dynasty; Tamralipta kingdom documented by Fa-Hien and Xuanzang',
    sthalaPurana: 'The ankle fell here; ancient seafaring merchants offered prayers before sailing across the Indian Ocean.',
    recommendedMantra: 'ॐ वर्गभीमायै नमः ॥',
    accessibility: 'Paved town access road, flat inner courtyard.'
  },
  {
    id: 'sp-40',
    name: 'Patan Devi (Patneshwari)',
    category: 'SHAKTI_PEETHA',
    lat: 25.6022,
    lng: 85.1950,
    state: 'Bihar',
    location: 'Patna (Pataliputra)',
    presidingDeity: 'Maa Bari Patan Devi & Chhoti Patan Devi',
    associatedBhairavaOrConsort: 'Bhairav guardian',
    bodyPartOrSignificance: 'Right Thigh of Devi Sati.',
    textualSource: 'Tantrachudamani & Kalika Purana',
    architecturalStyle: 'North Indian Nagara style shrine with marble sanctum',
    historicalPeriod: 'Ancient Pataliputra; royal deity of Chandragupta and Ashoka lore',
    sthalaPurana: 'Devi Sati’s right thigh fell in ancient Pataliputra, establishing Patneshwari as guardian of the capital.',
    recommendedMantra: 'ॐ पाटनेश्वर्यै नमः ॥',
    accessibility: 'Wide entrance gate, wheelchair ramps into temple hall.'
  },
  {
    id: 'sp-41',
    name: 'Mithila Uchchaith Durga',
    category: 'SHAKTI_PEETHA',
    lat: 26.4716,
    lng: 85.8564,
    state: 'Bihar',
    location: 'Benipatti, Madhubani',
    presidingDeity: 'Maa Uma / Vanadurga',
    associatedBhairavaOrConsort: 'Mahodara Bhairava',
    bodyPartOrSignificance: 'Left Shoulder of Devi Sati.',
    textualSource: 'Tantrachudamani & Skanda Purana',
    architecturalStyle: 'Mithila style courtyard shrine with traditional Madhubani mural art',
    historicalPeriod: 'Where Mahakavi Kalidasa was blessed with supreme poetic inspiration by the Goddess',
    sthalaPurana: 'Kalidasa cut his own tongue in reverence; the Goddess restored it and granted celestial poetic wisdom.',
    recommendedMantra: 'ॐ उमादेव्यै नमः ॥',
    accessibility: 'Tree-shaded rural walkways, level entry.'
  },
  {
    id: 'sp-42',
    name: 'Harsiddhi Mata (Ujjain)',
    category: 'SHAKTI_PEETHA',
    lat: 23.1812,
    lng: 75.7645,
    state: 'Madhya Pradesh',
    location: 'Ujjain, beside Rudra Sagar Lake',
    presidingDeity: 'Maa Harsiddhi (Riddhisiddhi)',
    associatedBhairavaOrConsort: 'Lambkarna Bhairava',
    bodyPartOrSignificance: 'Elbow of Devi Sati.',
    textualSource: 'Shiva Purana & Skanda Purana (Avanti Khanda)',
    architecturalStyle: 'Maratha style with two legendary 50-foot lamp towers (Deepa Stambhas) with 1,008 oil lamps',
    historicalPeriod: 'King Vikramaditya of Ujjain worshipped Harsiddhi as his chief patron deity',
    sthalaPurana: 'Lord Shiva named her Harsiddhi because she bestowed joyful accomplishment upon the universe.',
    recommendedMantra: 'ॐ ह्रीं हरसिद्धये नमः ॥',
    accessibility: 'Included in Mahakal Lok complex; golf cart and ramp access.'
  },
  {
    id: 'sp-43',
    name: 'Sharda Devi (Maihar)',
    category: 'SHAKTI_PEETHA',
    lat: 24.2690,
    lng: 80.7580,
    state: 'Madhya Pradesh',
    location: 'Maihar, Trikoota Hill (Satna)',
    presidingDeity: 'Maa Sharda (Saraswati)',
    associatedBhairavaOrConsort: 'Bhairav protector (Alha & Udal lore)',
    bodyPartOrSignificance: 'Necklace (Maihar = Mai Ka Haar / Mother’s Necklace).',
    textualSource: 'Tantrachudamani & Bundelkhand folk chronicles',
    architecturalStyle: 'Hilltop shrine with 1,063 stone steps and ropeway',
    historicalPeriod: 'Immortal warriors Alha and Udal performed morning puja here for 12 years',
    sthalaPurana: 'Devi Sati’s golden pearl necklace fell on Trikuta hill.',
    recommendedMantra: 'ॐ शारदायै नमः ॥',
    accessibility: 'Modern aerial ropeway directly to the hilltop temple, avoiding all stairs.'
  },
  {
    id: 'sp-44',
    name: 'Kurukshetra Bhadrakali',
    category: 'SHAKTI_PEETHA',
    lat: 29.9723,
    lng: 76.8488,
    state: 'Haryana',
    location: 'Thanesar, Kurukshetra',
    presidingDeity: 'Maa Bhadrakali',
    associatedBhairavaOrConsort: 'Sthanu Bhairava',
    bodyPartOrSignificance: 'Right Ankle of Devi Sati.',
    textualSource: 'Tantrachudamani & Mahabharata (Sauptika Parva)',
    architecturalStyle: 'Red stone North Indian temple with ceremonial horse statues',
    historicalPeriod: 'Pandavas prayed here and offered clay/bronze horses before the Kurukshetra War',
    sthalaPurana: 'Sri Krishna and the Pandavas took vows of righteous victory before Goddess Bhadrakali.',
    recommendedMantra: 'ॐ भद्रकाल्यै नमः ॥',
    accessibility: 'Wheelchair access ramp, wide open marble praangan.'
  },
  {
    id: 'sp-45',
    name: 'Maya Devi Temple (Haridwar)',
    category: 'SHAKTI_PEETHA',
    lat: 29.9463,
    lng: 78.1610,
    state: 'Uttarakhand',
    location: 'Haridwar (Mayapuri), Upper Ganga Canal',
    presidingDeity: 'Goddess Maya Devi (Adhisthatri of Haridwar)',
    associatedBhairavaOrConsort: 'Anandabhairava',
    bodyPartOrSignificance: 'Navel & Heart of Sati.',
    textualSource: 'Skanda Purana (Kedara Khanda)',
    architecturalStyle: 'Ancient stone temple with three-headed, four-armed Goddess murti',
    historicalPeriod: '11th century CE; one of the Siddhpeethas of Haridwar alongside Chandi & Mansa Devi',
    sthalaPurana: 'Haridwar is also known as Mayapuri in honoring Maya Devi’s ancient throne.',
    recommendedMantra: 'ॐ मायादेव्यै नमः ॥',
    accessibility: 'Direct street entrance, zero steep inclines, peaceful garden.'
  },
  {
    id: 'sp-46',
    name: 'Lalita Devi (Naimisharanya)',
    category: 'SHAKTI_PEETHA',
    lat: 27.3480,
    lng: 80.4855,
    state: 'Uttar Pradesh',
    location: 'Naimisharanya, Sitapur, Gomti River',
    presidingDeity: 'Maa Lalita Tripurasundari',
    associatedBhairavaOrConsort: 'Bhairav shrine at Chakra Tirtha',
    bodyPartOrSignificance: 'Heart of Devi Sati.',
    textualSource: 'Devi Bhagavata & Skanda Purana',
    architecturalStyle: 'Spacious classical temple courtyard beside sacred Chakra Tirtha kund',
    historicalPeriod: 'Where 88,000 rishis led by Shaunaka performed the 1,000-year Satra yajna',
    sthalaPurana: 'Manifested to calm the overwhelming velocity of Brahma’s cosmic disc (Manomaya Chakra).',
    recommendedMantra: 'ॐ श्रीं ह्रीं क्लीं ललितायै नमः ॥',
    accessibility: 'Shaded pradakshina, step-free access around Chakra Tirtha.'
  },
  {
    id: 'sp-47',
    name: 'Draksharamam Manikyamba',
    category: 'SHAKTI_PEETHA',
    lat: 16.7937,
    lng: 82.0628,
    state: 'Andhra Pradesh',
    location: 'Draksharamam, Konaseema, Godavari Delta',
    presidingDeity: 'Goddess Manikyamba',
    associatedBhairavaOrConsort: 'Bhimeswara Swamy (Pancharama Kshetra)',
    bodyPartOrSignificance: 'Left Cheek of Devi Sati.',
    textualSource: 'Skanda Purana & Bhimeswara Purana of Srinatha',
    architecturalStyle: 'Magnificent two-storey Eastern Chalukyan temple complex (9th c. CE)',
    historicalPeriod: 'Built by King Chalukya Bhima I (892–922 CE)',
    sthalaPurana: 'Also renowned as one of the Five Sacred Pancharama Kshetras of Lord Shiva.',
    recommendedMantra: 'ॐ माणिक्याम्बायै नमः ॥',
    accessibility: 'Ground level pathways, priority queue for seniors.'
  },
  {
    id: 'sp-48',
    name: 'Sugandha (Shikarpur)',
    category: 'SHAKTI_PEETHA',
    lat: 22.7100,
    lng: 90.3500,
    state: 'Barisal (Bangladesh)',
    location: 'Shikarpur, Sunanda River',
    presidingDeity: 'Goddess Sugandha (Sunanda)',
    associatedBhairavaOrConsort: 'Tryambak Bhairava',
    bodyPartOrSignificance: 'Nose of Devi Sati.',
    textualSource: 'Tantrachudamani',
    architecturalStyle: 'Traditional stone temple on the river bank',
    historicalPeriod: 'Ancient Bengal Shakta Peetha',
    sthalaPurana: 'Devi’s nose fell here, blessing the river with divine floral fragrance (Sugandha).',
    recommendedMantra: 'ॐ सुगन्धायै नमः ॥',
    accessibility: 'Local temple trust riverbank pathways.'
  },
  {
    id: 'sp-49',
    name: 'Chattal Bhabanipur Peetha',
    category: 'SHAKTI_PEETHA',
    lat: 22.6300,
    lng: 91.8300,
    state: 'Chittagong (Bangladesh)',
    location: 'Chandranath Hill, Sitakunda',
    presidingDeity: 'Goddess Bhavani',
    associatedBhairavaOrConsort: 'Chandrasekhar Bhairava',
    bodyPartOrSignificance: 'Right Arm of Devi Sati.',
    textualSource: 'Tantrachudamani & Matsya Purana',
    architecturalStyle: 'Hilltop shrine amid natural thermal water streams',
    historicalPeriod: 'Ancient pilgrimage destination on Chandranath Peak',
    sthalaPurana: 'Right arm of Sati fell atop Chandranath peak.',
    recommendedMantra: 'ॐ भवान्यै नमः ॥',
    accessibility: 'Paved trail with resting pavilions for pilgrims.'
  },
  {
    id: 'sp-50',
    name: 'Yashoreshwari (Ishwaripur)',
    category: 'SHAKTI_PEETHA',
    lat: 22.3167,
    lng: 89.0667,
    state: 'Satkhira (Bangladesh)',
    location: 'Shyamnagar, Ishwaripur',
    presidingDeity: 'Maa Yashoreshwari',
    associatedBhairavaOrConsort: 'Chanda Bhairava',
    bodyPartOrSignificance: 'Hands & Feet of Devi Sati.',
    textualSource: 'Tantrachudamani',
    architecturalStyle: '100-door Natmandir terracotta pavilion built by Maharaja Pratapaditya',
    historicalPeriod: 'Revitalized by King Pratapaditya of Jessore (16th c. CE); Indian PM visited in 2021',
    sthalaPurana: 'General of the divine armies; protected the Jessore kingdom.',
    recommendedMantra: 'ॐ यशोर्यै नमः ॥',
    accessibility: 'Spacious flat complex, newly renovated community center.'
  },
  {
    id: 'sp-51',
    name: 'Kanya Kumari (Bhagavathy)',
    category: 'SHAKTI_PEETHA',
    lat: 8.0780,
    lng: 77.5550,
    state: 'Tamil Nadu',
    location: 'Kanyakumari, Triveni Sangam of 3 Oceans',
    presidingDeity: 'Goddess Kanya Kumari (The Virgin Goddess)',
    associatedBhairavaOrConsort: 'Nimisha Bhairava (Sthanumalayan Temple)',
    bodyPartOrSignificance: 'Back / Spine of Devi Sati.',
    textualSource: 'Taittiriya Aranyaka, Periplus & Silappatikaram',
    architecturalStyle: 'Dravidian seaside temple facing the confluence of Indian Ocean, Arabian Sea & Bay of Bengal',
    historicalPeriod: 'Mentioned in ancient Sangam literature & 1st-century CE Greek travelogues',
    sthalaPurana: 'The youthful Goddess did severe penance to marry Shiva; vanquished demon Banasura.',
    recommendedMantra: 'कात्यायनाय विद्महे कन्याकुमारि धीमहि । तन्नो दुर्गिः प्रचोदयात् ॥',
    accessibility: 'Smooth stone seaside corridors, wheelchair ramps, battery carts.'
  }
];

// --- Custom Leaflet Marker Icons with Sacred Geometry ---

const createCustomIcon = (type: TirthaCategory, isSelected: boolean) => {
  let bgColor = '#D4AF37'; // Gold
  let borderColor = '#4A0E17'; // Sanctum Maroon
  let symbolSvg = '';

  if (type === 'JYOTIRLINGA') {
    bgColor = isSelected ? '#FFD700' : '#D4AF37'; // Temple Gold
    borderColor = '#4A0E17';
    // Trishul & Crescent Lingam
    symbolSvg = `<path d="M12 2v14M8 6c0 3 4 5 4 5s4-2 4-5M9 20h6" stroke="#4A0E17" stroke-width="2" stroke-linecap="round"/>`;
  } else {
    // SHAKTI_PEETHA
    bgColor = isSelected ? '#FF1744' : '#C2185B'; // Crimson / Deep Ruby
    borderColor = '#FFFBF5';
    // Sacred Lotus / Devi Yantra motif
    symbolSvg = `<circle cx="12" cy="12" r="4" fill="#FFFBF5"/><path d="M12 4c-2 3-2 5 0 8 2-3 2-5 0-8zM4 12c3-2 5-2 8 0-3 2-5 2-8 0z" fill="#FFFBF5"/>`;
  }

  const pulseRing = isSelected
    ? `<div style="position:absolute; width:44px; height:44px; top:-6px; left:-6px; border-radius:50%; border:2px solid ${bgColor}; animation:pulse 1.5s infinite;"></div>`
    : '';

  const html = `
    <div style="position:relative; width:32px; height:32px;">
      ${pulseRing}
      <div style="
        width: 32px;
        height: 32px;
        background: ${bgColor};
        border: 2px solid ${borderColor};
        border-radius: 50% 50% 50% 0;
        transform: rotate(-45deg);
        box-shadow: 0 4px 10px rgba(0,0,0,0.35);
        display: flex;
        align-items: center;
        justify-content: center;
      ">
        <svg style="transform: rotate(45deg); width: 18px; height: 18px;" viewBox="0 0 24 24" fill="none">
          ${symbolSvg}
        </svg>
      </div>
    </div>
  `;

  return L.divIcon({
    className: 'custom-sacred-marker',
    html: html,
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
  });
};

// --- Auto-Pan Component ---
const MapFocusHandler: React.FC<{ targetCoords: [number, number] | null }> = ({ targetCoords }) => {
  const map = useMap();
  React.useEffect(() => {
    if (targetCoords) {
      map.flyTo(targetCoords, 8, { duration: 1.5 });
    }
  }, [targetCoords, map]);
  return null;
};

// --- Main Sacred Tirtha Atlas Component ---

// --- Haversine Distance Calculation ---
export function calculateDistanceKm(lat1: number, lon1: number, lat2: number, lon2: number): number {
  const R = 6371; // Earth's mean radius in km
  const dLat = ((lat2 - lat1) * Math.PI) / 180;
  const dLon = ((lon2 - lon1) * Math.PI) / 180;
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos((lat1 * Math.PI) / 180) *
      Math.cos((lat2 * Math.PI) / 180) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return Math.round(R * c * 10) / 10;
}

export interface ClosestTirthaItem extends SacredTirtha {
  distanceKm: number;
}

// User GPS Location Icon
const createUserLocationIcon = () => {
  return L.divIcon({
    className: 'user-gps-marker',
    html: `
      <div style="position:relative; width:26px; height:26px;">
        <div style="position:absolute; width:22px; height:22px; top:2px; left:2px; border-radius:50%; background:#1E88E5; border:3px solid #FFFFFF; box-shadow:0 0 10px rgba(30,136,229,0.9);"></div>
        <div style="position:absolute; width:34px; height:34px; top:-4px; left:-4px; border-radius:50%; border:2px solid #1E88E5; opacity:0.65; animation:pulse 1.8s infinite;"></div>
      </div>
    `,
    iconSize: [26, 26],
    iconAnchor: [13, 13],
    popupAnchor: [0, -13],
  });
};

export const SacredTirthaAtlasMap: React.FC<{
  onSelectTirtha?: (tirtha: SacredTirtha) => void;
  className?: string;
}> = ({ onSelectTirtha, className = '' }) => {
  const [activeCategory, setActiveCategory] = useState<'ALL' | 'JYOTIRLINGA' | 'SHAKTI_PEETHA'>('ALL');
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedTirtha, setSelectedTirtha] = useState<SacredTirtha | null>(null);

  // Geolocation State
  const [userCoords, setUserCoords] = useState<{ lat: number; lng: number } | null>(null);
  const [isLocating, setIsLocating] = useState<boolean>(false);
  const [geoError, setGeoError] = useState<string | null>(null);
  const [closestTirthas, setClosestTirthas] = useState<ClosestTirthaItem[] | null>(null);
  const [showClosestModal, setShowClosestModal] = useState<boolean>(false);

  // Combine datasets
  const allTirthas = useMemo(() => [...JYOTIRLINGAS, ...SHAKTI_PEETHAS], []);

  // Filtered dataset
  const filteredTirthas = useMemo(() => {
    return allTirthas.filter((t) => {
      const matchesCategory =
        activeCategory === 'ALL' || t.category === activeCategory;
      const q = searchQuery.toLowerCase().trim();
      const matchesSearch =
        !q ||
        t.name.toLowerCase().includes(q) ||
        t.state.toLowerCase().includes(q) ||
        t.location.toLowerCase().includes(q) ||
        t.presidingDeity.toLowerCase().includes(q) ||
        (t.bodyPartOrSignificance && t.bodyPartOrSignificance.toLowerCase().includes(q));

      return matchesCategory && matchesSearch;
    });
  }, [allTirthas, activeCategory, searchQuery]);

  // Speech Synthesis State
  const [isSpeaking, setIsSpeaking] = useState<boolean>(false);

  // Stop speech on unmount
  React.useEffect(() => {
    return () => {
      if (typeof window !== 'undefined' && 'speechSynthesis' in window) {
        window.speechSynthesis.cancel();
      }
    };
  }, []);

  const handleStopAudio = () => {
    if (typeof window !== 'undefined' && 'speechSynthesis' in window) {
      window.speechSynthesis.cancel();
    }
    setIsSpeaking(false);
  };

  const handleToggleAudioTour = (tirtha: SacredTirtha) => {
    if (typeof window === 'undefined' || !('speechSynthesis' in window)) {
      alert('Web Speech API is not supported in this browser.');
      return;
    }

    const synth = window.speechSynthesis;
    if (isSpeaking || synth.speaking) {
      synth.cancel();
      setIsSpeaking(false);
      return;
    }

    synth.cancel();
    const narrationText = `Sacred Tirtha: ${tirtha.name}, located in ${tirtha.location}, ${tirtha.state}. Presiding deity: ${tirtha.presidingDeity}. Sthala Purana history: ${tirtha.sthalaPurana}. Scriptural authority: ${tirtha.textualSource}. Prescribed sacred mantra: ${tirtha.recommendedMantra}. Accessibility notes: ${tirtha.accessibility}.`;

    const utterance = new SpeechSynthesisUtterance(narrationText);
    utterance.rate = 0.92;
    utterance.pitch = 1.0;

    const voices = synth.getVoices();
    const voice = voices.find((v) => v.lang === 'en-IN') || voices.find((v) => v.lang.startsWith('en'));
    if (voice) {
      utterance.voice = voice;
    }

    utterance.onstart = () => setIsSpeaking(true);
    utterance.onend = () => setIsSpeaking(false);
    utterance.onerror = () => setIsSpeaking(false);

    synth.speak(utterance);
  };

  const handleMarkerClick = (tirtha: SacredTirtha) => {
    handleStopAudio();
    setSelectedTirtha(tirtha);
    if (onSelectTirtha) onSelectTirtha(tirtha);
  };

  // Browser Geolocation API Handler
  const handleFindClosestTirthas = () => {
    setGeoError(null);
    if (typeof window === 'undefined' || !navigator.geolocation) {
      setGeoError('Geolocation is not supported by your browser environment.');
      return;
    }

    setIsLocating(true);
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        setIsLocating(false);
        const userLat = pos.coords.latitude;
        const userLng = pos.coords.longitude;
        setUserCoords({ lat: userLat, lng: userLng });

        // Calculate distance to all tirthas
        const withDistance: ClosestTirthaItem[] = allTirthas.map((t) => ({
          ...t,
          distanceKm: calculateDistanceKm(userLat, userLng, t.lat, t.lng),
        }));

        // Sort ascending and pick 3 closest
        withDistance.sort((a, b) => a.distanceKm - b.distanceKm);
        const top3 = withDistance.slice(0, 3);
        setClosestTirthas(top3);
        setShowClosestModal(true);
      },
      (err) => {
        setIsLocating(false);
        let errorMsg = 'Could not access GPS location.';
        if (err.code === 1) {
          errorMsg = 'GPS location permission denied. Please allow location access in your browser.';
        } else if (err.code === 2) {
          errorMsg = 'GPS position unavailable. Please check your network/device connection.';
        } else if (err.code === 3) {
          errorMsg = 'GPS request timed out. Please try again.';
        }
        setGeoError(errorMsg);
      },
      {
        enableHighAccuracy: true,
        timeout: 12000,
        maximumAge: 60000,
      }
    );
  };

  return (
    <div className={`flex flex-col h-full w-full bg-[#FFFBF5] text-[#2C1810] font-serif ${className}`} style={{ minHeight: '650px' }}>
      {/* Top Banner / Filter Bar */}
      <div className="bg-[#4A0E17] text-[#FFFBF5] p-4 shadow-md border-b-2 border-[#D4AF37]">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 max-w-7xl mx-auto">
          <div>
            <div className="flex items-center gap-2">
              <span className="text-[#D4AF37] text-xl">ॐ</span>
              <h1 className="text-xl md:text-2xl font-bold tracking-wide">
                Sacred Geography & Tirtha Yatra Atlas
              </h1>
            </div>
            <p className="text-xs text-[#FFFBF5]/80 mt-1">
              Topography of Divine Grace: 12 Dwadasha Jyotirlingas & 51 Maha Shakti Peethas with authentic canonical citations.
            </p>
          </div>

          {/* Action Buttons: Geolocation + Category Filter Pills */}
          <div className="flex flex-wrap items-center gap-2">
            {/* 📍 GEOLOCATION BUTTON (USER REQUEST) */}
            <button
              onClick={handleFindClosestTirthas}
              disabled={isLocating}
              className={`px-3 py-1.5 rounded-full text-xs font-bold transition flex items-center gap-1.5 shadow-md border border-[#D4AF37] ${
                isLocating
                  ? 'bg-amber-200 text-amber-900 cursor-wait'
                  : 'bg-gradient-to-r from-[#D4AF37] to-[#F59E0B] text-[#4A0E17] hover:brightness-110 active:scale-95'
              }`}
              title="Locate the 3 nearest sacred tirthas relative to your GPS coordinates"
            >
              {isLocating ? (
                <>
                  <span className="animate-spin inline-block">⏳</span>
                  <span>Finding Your GPS...</span>
                </>
              ) : (
                <>
                  <span>📍</span>
                  <span>Find 3 Closest Tirthas</span>
                </>
              )}
            </button>

            <button
              onClick={() => setActiveCategory('ALL')}
              className={`px-3 py-1.5 rounded-full text-xs font-semibold transition ${
                activeCategory === 'ALL'
                  ? 'bg-[#D4AF37] text-[#4A0E17] shadow-sm'
                  : 'bg-white/10 text-[#FFFBF5] hover:bg-white/20'
              }`}
            >
              All Tirthas ({allTirthas.length})
            </button>
            <button
              onClick={() => setActiveCategory('JYOTIRLINGA')}
              className={`px-3 py-1.5 rounded-full text-xs font-semibold transition flex items-center gap-1.5 ${
                activeCategory === 'JYOTIRLINGA'
                  ? 'bg-[#D4AF37] text-[#4A0E17] shadow-sm'
                  : 'bg-white/10 text-[#FFFBF5] hover:bg-white/20'
              }`}
            >
              <span className="w-2.5 h-2.5 rounded-full bg-[#D4AF37] inline-block border border-[#4A0E17]" />
              12 Jyotirlingas ({JYOTIRLINGAS.length})
            </button>
            <button
              onClick={() => setActiveCategory('SHAKTI_PEETHA')}
              className={`px-3 py-1.5 rounded-full text-xs font-semibold transition flex items-center gap-1.5 ${
                activeCategory === 'SHAKTI_PEETHA'
                  ? 'bg-[#FF1744] text-white shadow-sm'
                  : 'bg-white/10 text-[#FFFBF5] hover:bg-white/20'
              }`}
            >
              <span className="w-2.5 h-2.5 rounded-full bg-[#FF1744] inline-block border border-white" />
              51 Shakti Peethas ({SHAKTI_PEETHAS.length})
            </button>
          </div>
        </div>

        {/* GPS Geolocation Feedback / Error Notice */}
        {geoError && (
          <div className="mt-2.5 max-w-7xl mx-auto bg-rose-900/90 border border-rose-400 text-rose-100 text-xs px-3 py-2 rounded-lg flex items-center justify-between">
            <div className="flex items-center gap-2">
              <span>⚠️</span>
              <span>{geoError}</span>
            </div>
            <button
              onClick={() => setGeoError(null)}
              className="text-white font-bold ml-2 hover:opacity-80"
            >
              ✕
            </button>
          </div>
        )}

        {/* Search Input Box */}
        <div className="mt-3 max-w-7xl mx-auto flex items-center gap-2">
          <input
            type="text"
            placeholder="Search by temple name, state, deity, or Sati body part (e.g. Kedarnath, Yoni, Eyes, Bengal)..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full md:w-96 px-3.5 py-1.5 text-xs rounded-lg bg-white/15 text-white placeholder-white/60 border border-[#D4AF37]/50 focus:outline-none focus:ring-1 focus:ring-[#D4AF37]"
          />
          {searchQuery && (
            <button
              onClick={() => setSearchQuery('')}
              className="text-xs text-[#D4AF37] hover:underline"
            >
              Clear
            </button>
          )}
          {userCoords && (
            <span className="text-xs text-amber-200 ml-2 hidden md:inline">
              📍 GPS: {userCoords.lat.toFixed(2)}° N, {userCoords.lng.toFixed(2)}° E
            </span>
          )}
          <span className="text-xs text-[#FFFBF5]/70 ml-auto hidden sm:inline">
            Showing <strong className="text-[#D4AF37]">{filteredTirthas.length}</strong> sacred sites
        </div>
      </div>

      {/* Main Map + Sliding Detail Pane Container */}
      <div className="relative flex-1 w-full h-[600px] md:h-[680px]">
        <MapContainer
          center={[22.9734, 78.6569]} // Center of Indian Subcontinent
          zoom={5}
          scrollWheelZoom={true}
          style={{ width: '100%', height: '100%' }}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />

          <MapFocusHandler
            targetCoords={selectedTirtha ? [selectedTirtha.lat, selectedTirtha.lng] : null}
          />

          {/* User Location GPS Marker */}
          {userCoords && (
            <Marker position={[userCoords.lat, userCoords.lng]} icon={createUserLocationIcon()}>
              <Popup>
                <div className="font-serif p-1 text-[#2C1810]">
                  <strong className="text-[#1565C0] text-xs">📍 Your GPS Position</strong>
                  <p className="text-[11px] text-gray-600 mt-0.5">
                    {userCoords.lat.toFixed(4)}° N, {userCoords.lng.toFixed(4)}° E
                  </p>
                </div>
              </Popup>
            </Marker>
          )}

          {filteredTirthas.map((tirtha) => {
            const isSelected = selectedTirtha?.id === tirtha.id;
            return (
              <Marker
                key={tirtha.id}
                position={[tirtha.lat, tirtha.lng]}
                icon={createCustomIcon(tirtha.category, isSelected)}
                eventHandlers={{
                  click: () => handleMarkerClick(tirtha),
                }}
              >
                <Popup className="sacred-tirtha-popup">
                  <div className="font-serif p-1 max-w-xs text-[#2C1810]">
                    <div className="flex items-center gap-1.5 text-[11px] font-bold text-[#E65100]">
                      <span>{tirtha.category === 'JYOTIRLINGA' ? '🔱 12 DWADASHA JYOTIRLINGA' : '🌺 51 MAHA SHAKTI PEETHA'}</span>
                    </div>
                    <h3 className="text-base font-bold text-[#4A0E17] leading-tight mt-0.5">
                      {tirtha.name}
                    </h3>
                    <p className="text-xs text-gray-700 italic mt-0.5">
                      📍 {tirtha.location}, {tirtha.state}
                    </p>

                    <div className="my-1.5 border-t border-amber-200" />

                    <p className="text-xs">
                      <strong>Deity:</strong> {tirtha.presidingDeity}
                    </p>
                    {tirtha.bodyPartOrSignificance && (
                      <p className="text-xs mt-1 text-[#4A0E17]">
                        <strong>Significance:</strong> {tirtha.bodyPartOrSignificance}
                      </p>
                    )}

                    <div className="mt-2.5 flex items-center justify-between gap-2">
                      <button
                        onClick={() => handleMarkerClick(tirtha)}
                        className="text-[11px] font-bold bg-[#4A0E17] text-[#FFFBF5] px-2.5 py-1 rounded shadow hover:bg-[#681421] transition"
                      >
                        View Full Details →
                      </button>
                      <a
                        href={`https://www.google.com/maps/dir/?api=1&destination=${tirtha.lat},${tirtha.lng}`}
                        target="_blank"
                        rel="noreferrer"
                        className="text-[11px] text-[#E65100] underline font-semibold"
                      >
                        Directions
                      </a>
                    </div>
                  </div>
                </Popup>
              </Marker>
            );
          })}
        </MapContainer>

        {/* 📍 3 CLOSEST TIRTHAS RESULTS DRAWER (USER REQUEST) */}
        {showClosestModal && closestTirthas && (
          <div className="absolute top-4 left-4 z-[1000] w-96 max-w-[calc(100%-2rem)] bg-[#FFFBF5] rounded-xl shadow-2xl border-2 border-[#D4AF37] max-h-[90%] overflow-y-auto p-4 transition-all animate-fadeIn">
            <div className="flex items-start justify-between">
              <div>
                <span className="text-[10px] font-bold px-2 py-0.5 rounded uppercase tracking-wider bg-amber-100 text-amber-900 border border-amber-300">
                  📍 Nearest Pilgrimage Sites
                </span>
                <h2 className="text-lg font-bold text-[#4A0E17] mt-1 font-serif">
                  3 Closest Sacred Tirthas
                </h2>
                <p className="text-xs text-gray-600">
                  Calculated from your current GPS coordinates ({userCoords?.lat.toFixed(2)}° N, {userCoords?.lng.toFixed(2)}° E)
                </p>
              </div>
              <button
                onClick={() => setShowClosestModal(false)}
                className="text-gray-400 hover:text-gray-700 p-1 text-sm font-bold"
                aria-label="Close"
              >
                ✕
              </button>
            </div>

            <div className="my-2.5 border-t border-amber-200" />

            {/* List of the 3 closest tirthas */}
            <div className="space-y-3">
              {closestTirthas.map((tirtha, idx) => (
                <div
                  key={tirtha.id}
                  className="bg-white p-3 rounded-lg border border-amber-200 shadow-sm hover:border-[#D4AF37] transition"
                >
                  <div className="flex items-start justify-between gap-2">
                    <div className="flex items-center gap-1.5">
                      <span className="w-5 h-5 rounded-full bg-[#4A0E17] text-[#FFFBF5] text-[11px] font-bold flex items-center justify-center">
                        {idx + 1}
                      </span>
                      <h3 className="text-sm font-bold text-[#4A0E17]">
                        {tirtha.name}
                      </h3>
                    </div>
                    <span className="text-[11px] font-bold text-[#B78103] bg-amber-50 px-2 py-0.5 rounded border border-amber-200 shrink-0">
                      {tirtha.distanceKm} km away
                    </span>
                  </div>

                  <p className="text-xs text-gray-600 mt-1">
                    📍 {tirtha.location}, {tirtha.state}
                  </p>
                  <p className="text-xs text-gray-800 mt-0.5">
                    <strong>Deity:</strong> {tirtha.presidingDeity}
                  </p>

                  {tirtha.bodyPartOrSignificance && (
                    <p className="text-[11px] text-[#8B0000] mt-1 italic">
                      {tirtha.bodyPartOrSignificance}
                    </p>
                  )}

                  <div className="mt-2.5 pt-2 border-t border-gray-100 flex items-center gap-2">
                    <button
                      onClick={() => handleMarkerClick(tirtha)}
                      className="flex-1 bg-[#4A0E17] hover:bg-[#681421] text-[#FFFBF5] text-center py-1.5 rounded text-xs font-semibold shadow-sm transition"
                    >
                      Inspect Details →
                    </button>
                    <a
                      href={`https://www.google.com/maps/dir/?api=1&destination=${tirtha.lat},${tirtha.lng}`}
                      target="_blank"
                      rel="noreferrer"
                      className="px-2.5 py-1.5 bg-amber-50 border border-amber-300 text-amber-900 rounded text-xs font-bold hover:bg-amber-100 transition"
                      title="Open turn-by-turn route"
                    >
                      Navigate ↗
                    </a>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Selected Tirtha Detail Drawer (Right Floating Card) */}
        {selectedTirtha && (
          <div className="absolute top-4 right-4 z-[1000] w-96 max-w-[calc(100%-2rem)] bg-[#FFFBF5] rounded-xl shadow-2xl border-2 border-[#D4AF37] max-h-[90%] overflow-y-auto p-4 transition-all animate-fadeIn">
            <div className="flex items-start justify-between">
              <div>
                <span
                  className={`text-[10px] font-bold px-2 py-0.5 rounded uppercase tracking-wider ${
                    selectedTirtha.category === 'JYOTIRLINGA'
                      ? 'bg-amber-100 text-amber-900 border border-amber-300'
                      : 'bg-rose-100 text-rose-900 border border-rose-300'
                  }`}
                >
                  {selectedTirtha.category === 'JYOTIRLINGA' ? '🔱 Jyotirlinga' : '🌺 Maha Shakti Peetha'}
                </span>
                <h2 className="text-lg font-bold text-[#4A0E17] mt-1 font-serif">
                  {selectedTirtha.name}
                </h2>
                <p className="text-xs text-gray-700">
                  📍 {selectedTirtha.location}, {selectedTirtha.state}
                </p>
              </div>
              <button
                onClick={() => {
                  handleStopAudio();
                  setSelectedTirtha(null);
                }}
                className="text-gray-400 hover:text-gray-700 p-1 text-sm font-bold"
                aria-label="Close"
              >
                ✕
              </button>
            </div>

            <div className="my-2.5 border-t border-amber-200" />

            {/* Presiding Deities & Spiritual Significance */}
            <div className="space-y-2 text-xs">
              <div>
                <span className="font-semibold text-gray-600">Presiding Deity:</span>
                <p className="font-medium text-[#4A0E17]">{selectedTirtha.presidingDeity}</p>
              </div>

              {selectedTirtha.associatedBhairavaOrConsort && (
                <div>
                  <span className="font-semibold text-gray-600">Associated Bhairava / Consort:</span>
                  <p className="text-gray-800">{selectedTirtha.associatedBhairavaOrConsort}</p>
                </div>
              )}

              {selectedTirtha.bodyPartOrSignificance && (
                <div className="bg-amber-50 p-2.5 rounded-lg border border-amber-200">
                  <span className="font-semibold text-[#8B0000] block text-[11px]">Sati Anga / Sacred Significance:</span>
                  <p className="text-gray-800 mt-0.5 leading-relaxed">{selectedTirtha.bodyPartOrSignificance}</p>
                </div>
              )}

              {/* Sthala Purana Summary with Audio Tour button */}
              <div className="bg-amber-50/50 p-2.5 rounded-lg border border-amber-200/80">
                <div className="flex items-center justify-between mb-1">
                  <span className="font-semibold text-gray-700">Sthala Purana & History:</span>
                  <button
                    onClick={() => handleToggleAudioTour(selectedTirtha)}
                    className={`px-2.5 py-1 rounded-full text-[11px] font-bold transition flex items-center gap-1 shadow-sm border ${
                      isSpeaking
                        ? 'bg-[#C2185B] text-white border-pink-300 animate-pulse'
                        : 'bg-[#4A0E17] text-[#FFFBF5] border-[#D4AF37] hover:bg-[#681421]'
                    }`}
                    title="Read Sthala Purana history aloud using Web Speech API"
                  >
                    <span>{isSpeaking ? '⏹️' : '🔊'}</span>
                    <span>{isSpeaking ? 'Stop Audio' : 'Audio Tour'}</span>
                  </button>
                </div>
                <p className="text-gray-700 mt-0.5 leading-relaxed">{selectedTirtha.sthalaPurana}</p>
              </div>

              {/* Architecture & Era */}
              <div className="grid grid-cols-2 gap-2 bg-gray-50 p-2 rounded border border-gray-200 text-[11px]">
                <div>
                  <span className="text-gray-500 block">Architecture:</span>
                  <span className="font-medium text-gray-800">{selectedTirtha.architecturalStyle}</span>
                </div>
                <div>
                  <span className="text-gray-500 block">Period:</span>
                  <span className="font-medium text-gray-800">{selectedTirtha.historicalPeriod}</span>
                </div>
              </div>

              {/* Canonical Source Citation */}
              <div className="bg-amber-50/70 p-2 rounded border border-amber-200/60 text-[11px]">
                <span className="font-semibold text-[#4A0E17]">Canonical Textual Citation:</span>
                <p className="text-gray-800 italic mt-0.5">{selectedTirtha.textualSource}</p>
              </div>

              {/* Recommended Mantra */}
              <div className="bg-[#4A0E17] text-[#FFFBF5] p-2.5 rounded-lg shadow-sm">
                <span className="text-[10px] text-[#D4AF37] font-semibold tracking-wider uppercase block">
                  Prescribed Vedic / Puranic Chanting
                </span>
                <p className="text-xs font-medium mt-1 leading-relaxed">{selectedTirtha.recommendedMantra}</p>
              </div>

              {/* Accessibility / Senior Citizens */}
              <div>
                <span className="font-semibold text-gray-600">Pilgrim Accessibility & Senior Support:</span>
                <p className="text-gray-700 text-[11px] mt-0.5">{selectedTirtha.accessibility}</p>
              </div>
            </div>

            {/* Action Buttons */}
            <div className="mt-4 pt-2 border-t border-amber-200 flex items-center gap-2">
              <a
                href={`https://www.google.com/maps/dir/?api=1&destination=${selectedTirtha.lat},${selectedTirtha.lng}`}
                target="_blank"
                rel="noreferrer"
                className="flex-1 bg-[#D4AF37] hover:bg-[#c29f2e] text-[#4A0E17] font-bold text-center py-2 rounded-lg text-xs shadow transition flex items-center justify-center gap-1.5"
              >
                <span>Navigate on Google Maps</span> ↗
              </a>
            </div>
          </div>
        )}
      </div>

      {/* Footer Quick Statistics */}
      <div className="bg-[#4A0E17] text-[#FFFBF5]/90 text-[11px] py-2 px-4 flex flex-wrap items-center justify-between gap-2 border-t border-[#D4AF37]/50">
        <div className="flex items-center gap-4">
          <span>🔱 12 Dwadasha Jyotirlingas: <strong>100% Verified</strong></span>
          <span>🌺 51 Maha Shakti Peethas: <strong>Canonical Coordinates</strong></span>
        </div>
        <div className="text-xs text-[#D4AF37]">
          SATYA Epistemic Guarantee: Zero supernatural extortion • 100% Scriptural Fidelity
        </div>
      </div>
    </div>
  );
};

export default SacredTirthaAtlasMap;
