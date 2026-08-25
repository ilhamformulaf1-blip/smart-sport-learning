package com.example.data.seed

import com.example.data.local.AppDatabase
import com.example.data.local.BadgeEntity
import com.example.data.local.MaterialEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.ProgressEntity
import com.example.data.local.QuizQuestionEntity
import com.example.data.local.UserBadgeEntity
import com.example.data.local.UserEntity
import com.example.data.local.VideoEntity
import com.example.model.LearningStatus
import com.example.model.NotificationType
import com.example.model.SportCategory
import com.example.model.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

object DatabaseSeeder {

    suspend fun seedDatabaseIfEmpty(database: AppDatabase) = withContext(Dispatchers.IO) {
        val userCount = database.userDao().getUserCount().first()
        if (userCount > 0) return@withContext

        // 1. Seed Users
        val defaultUsers = listOf(
            UserEntity(
                id = 1,
                name = "Ilham Kurniawan",
                email = "siswa@smartsport.sch.id",
                passwordHash = "123456",
                role = UserRole.SISWA,
                kelas = "Kelas X MIPA 1",
                photoUrl = "avatar_student_1",
                totalScore = 480,
                completedCount = 3
            ),
            UserEntity(
                id = 2,
                name = "Pak Budi Santoso, M.Pd.",
                email = "guru@smartsport.sch.id",
                passwordHash = "123456",
                role = UserRole.GURU,
                kelas = "Guru PJOK SMA",
                photoUrl = "avatar_teacher",
                totalScore = 1200,
                completedCount = 28
            ),
            UserEntity(
                id = 3,
                name = "Admin PJOK Pusat",
                email = "admin@smartsport.sch.id",
                passwordHash = "admin123",
                role = UserRole.ADMIN,
                kelas = "Administrator Sistem",
                photoUrl = "avatar_admin",
                totalScore = 2000,
                completedCount = 30
            ),
            UserEntity(
                id = 4,
                name = "Siti Rahmawati",
                email = "siti@smartsport.sch.id",
                passwordHash = "123456",
                role = UserRole.SISWA,
                kelas = "Kelas X IPS 2",
                photoUrl = "avatar_student_2",
                totalScore = 420,
                completedCount = 2
            ),
            UserEntity(
                id = 5,
                name = "Dimas Arya Pratama",
                email = "dimas@smartsport.sch.id",
                passwordHash = "123456",
                role = UserRole.SISWA,
                kelas = "Kelas XI MIPA 3",
                photoUrl = "avatar_student_3",
                totalScore = 390,
                completedCount = 2
            )
        )
        for (u in defaultUsers) {
            database.userDao().insertUser(u)
        }

        // 2. Seed Badges
        val badges = listOf(
            BadgeEntity(1, "sport_learner", "Sport Learner", "Menyelesaikan materi pembelajaran pertama kamu", "emoji_events", "Selesaikan 1 materi", "Umum"),
            BadgeEntity(2, "football_expert", "Football Expert", "Menguasai seluruh modul teknik sepak bola", "sports_soccer", "Selesaikan materi sepak bola", "Sepak Bola"),
            BadgeEntity(3, "basketball_player", "Basketball Player", "Menuntaskan materi bola basket modern", "sports_basketball", "Selesaikan materi bola basket", "Bola Basket"),
            BadgeEntity(4, "volleyball_expert", "Volleyball Expert", "Menguasai teknik dasar & taktik bola voli", "sports_volleyball", "Selesaikan materi bola voli", "Bola Voli"),
            BadgeEntity(5, "athletics_master", "Athletics Master", "Menyelesaikan seluruh cabang olahraga atletik", "directions_run", "Selesaikan seluruh modul atletik", "Atletik"),
            BadgeEntity(6, "first_aid_hero", "First Aid Hero", "Memahami penanganan cedera & prinsip RICE", "medical_services", "Selesaikan penanganan cedera", "Penanganan Cedera"),
            BadgeEntity(7, "quiz_champion", "Quiz Champion", "Mendapatkan nilai >= 90 pada kuis interaktif", "local_fire_department", "Nilai kuis >= 90", "Kuis"),
            BadgeEntity(8, "smart_sport_master", "Smart Sport Master", "Menuntaskan seluruh pembelajaran & latihan", "school", "Selesaikan seluruh kurikulum PJOK", "Master")
        )
        database.badgeDao().insertBadges(badges)

        // Seed initial student unlocked badges
        database.badgeDao().unlockBadge(UserBadgeEntity(0, 1, "sport_learner", System.currentTimeMillis() - 86400000))
        database.badgeDao().unlockBadge(UserBadgeEntity(0, 1, "football_expert", System.currentTimeMillis() - 43200000))

        // 3. Seed Materials
        val materials = mutableListOf<MaterialEntity>()

        // --- SEPAK BOLA ---
        materials.add(
            MaterialEntity(
                id = 1,
                category = SportCategory.SEPAK_BOLA,
                title = "Passing dan Control (Mengumpan & Menghentikan Bola)",
                shortDescription = "Teknik esensial passing kaki bagian dalam, luar, punggung kaki, serta cara control bola akurat.",
                learningObjectives = "1. Siswa mampu mempraktikkan passing kaki dalam dengan akurasi tinggi.\n2. Siswa dapat mengontrol bola pantul dan datar secara efisien.\n3. Siswa memahami timing pelepasan bola.",
                contentBody = "Passing dan control adalah fondasi utama permainan sepak bola modern (tiki-taka maupun direct play). Passing yang presisi memungkinkan sirkulasi bola yang cepat dan membuka ruang pertahanan lawan. Control yang baik (first touch) menentukan kecepatan aksi berikutnya pemain.",
                basicTechniques = "1. Posisi Tubuh: Berdiri menghadap target dengan kaki tumpu sejajar bola (jarak 10-15 cm).\n2. Ayunan Kaki: Putar pergelangan kaki keluar untuk passing kaki dalam, ayunkan dari belakang ke depan.\n3. Kontak Bola: Sentuh bagian tengah bola agar laju datar menyusur rumput.\n4. Follow-through: Lanjutkan gerakan ayunan ke arah sasaran.\n5. Control: Rilekskan kaki saat menyentuh bola untuk meredam momentum.",
                commonMistakes = "1. Kaki tumpu terlalu jauh dari bola sehingga passing melayang.\n2. Badan terlalu kaku dan tidak ada gerakan lanjutan (follow through).\n3. Kaki menyentuh bagian bawah bola sehingga bola melambung liar.",
                tips = "Lakukan latihan dinding (wall pass) 50 kali setiap kaki setiap hari untuk melatih reflek first touch.",
                practiceExercises = "Latihan Passing Berpasangan 2 Sentuhan (jarak 5-10 meter) selama 15 menit.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=0kF4_7qT2tM",
                readTimeMinutes = 6,
                orderIndex = 1,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 2,
                category = SportCategory.SEPAK_BOLA,
                title = "Dribbling dan Shooting (Menggiring & Menembak Bola)",
                shortDescription = "Kuasai seni menggiring bola dengan lincah serta teknik shooting keras akurat ke gawang.",
                learningObjectives = "1. Menguasai dribbling zigzag menggunakan kaki bagian dalam dan luar.\n2. Menembak bola menggunakan kura-kura kaki (instep drive).\n3. Mengembangkan insting mencetak gol.",
                contentBody = "Dribbling berguna untuk melewati pemain bertahan, mengubah tempo permainan, dan mencari celah tembakan. Shooting adalah penyelesaian akhir (finishing) untuk menghasilkan gol kemenangan.",
                basicTechniques = "1. Dribble: Jaga bola tetap dekat dengan kaki (30-50 cm), pandangan bergantian antara bola dan lapangan.\n2. Shooting Power: Gunakan punggung kaki (tali sepatu), kunci pergelangan kaki ke bawah, condongkan dada ke depan saat menembak.\n3. Placement Shooting: Gunakan kaki dalam dengan plessing melengkung mengincar sudut gawang.",
                commonMistakes = "1. Menunduk terus menerus saat dribble tanpa melihat rekan atau lawan.\n2. Tubuh terlalu condong ke belakang saat shooting sehingga bola melambung ke atas mistar.",
                tips = "Kunci pergelangan kaki dengan kokoh pada saat benturan dengan bola untuk transfer tenaga maksimal.",
                practiceExercises = "Dribbling melewati 6 cone zigzag dilanjutkan shooting 1 sentuhan ke gawang.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=1oW_W1N_Qc8",
                readTimeMinutes = 7,
                orderIndex = 2,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 3,
                category = SportCategory.SEPAK_BOLA,
                title = "Heading dan Throw-in (Menyundul & Lemparan ke Dalam)",
                shortDescription = "Teknik menyundul bola dengan dahi secara bertenaga serta aturan lemparan ke dalam yang sah.",
                learningObjectives = "1. Melakukan heading menggunakan dahi dengan mata tetap terbuka.\n2. Memahami aturan legal throw-in menurut Laws of the Game IFAB.\n3. Melakukan lemparan jarak jauh bertenaga.",
                contentBody = "Heading digunakan untuk duel udara, menghalau umpan silang lawan (clearance), maupun mencetak gol tandukan. Throw-in adalah restart permainan setelah bola keluar garis samping.",
                basicTechniques = "1. Heading: Gunakan tulang dahi (forehead), kuatkan otot leher, lompat dengan tolakan satu atau dua kaki, mata jangan memejam.\n2. Throw-in: Kedua kaki harus menyentuh tanah di luar/pada garis, bola dipegang dengan kedua tangan ditarik dari belakang kepala melewati atas kepala.",
                commonMistakes = "1. Memejamkan mata saat menyundul sehingga bola mengenai ubun-ubun yang berbahaya.\n2. Kaki terangkat dari tanah saat melakukan lemparan ke dalam (foul throw).",
                tips = "Latih otot leher dan lompatan plyometrik secara berkala untuk duel udara yang kokoh.",
                practiceExercises = "Latihan sundulan berpasangan sambil melompat 20 repetisi.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=3gA8aR2yM9s",
                readTimeMinutes = 5,
                orderIndex = 3,
                isPopular = false
            )
        )
        materials.add(
            MaterialEntity(
                id = 4,
                category = SportCategory.SEPAK_BOLA,
                title = "Teknik Penjaga Gawang (Goalkeeping & Positioning)",
                shortDescription = "Posisi dasar kiper, tangkapan bola atas/bawah, diving aman, dan distribusi bola.",
                learningObjectives = "1. Menguasai set-position siap siaga penjaga gawang.\n2. Melakukan tangkapan W-catch dan scoop catch dengan aman.\n3. Melakukan diving dan mendarat tanpa cedera.",
                contentBody = "Kiper adalah garis pertahanan terakhir sekaligus inisiator serangan pertama. Kiper modern dituntut piawai memotong bola atas, melakukan diving, serta memiliki kemampuan ball distribution yang presisi.",
                basicTechniques = "1. Set Position: Buka kaki selebar bahu, lutut sedikit ditekuk, berat badan di ujung telapak kaki, tangan setinggi pinggang terbuka.\n2. Tangkapan Atas (W-Catch): Bentuk huruf W dengan kedua ibu jari dan telunjuk di belakang bola.\n3. Tangkapan Bawah (Scoop): Rapatkan kaki atau tekuk satu lutut untuk memblokir laju bola di tanah.",
                commonMistakes = "1. Menangkap bola keras tanpa membentuk bantalan tangan sehingga bola terlepas (blunder).\n2. Mendarat dengan siku atau pinggul keras saat diving tanpa berguling menyerap benturan.",
                tips = "Komunikasikan setiap pergerakan dengan bek ('KIPER!' atau 'BUANG!') dengan suara lantang dan percaya diri.",
                practiceExercises = "Latihan tangkapan bola lambung dan bola menyusur tanah masing-masing 15 kali.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=kYv98pL7kGg",
                readTimeMinutes = 6,
                orderIndex = 4,
                isPopular = false
            )
        )
        materials.add(
            MaterialEntity(
                id = 5,
                category = SportCategory.SEPAK_BOLA,
                title = "Taktik, Peraturan & Perwasitan Sepak Bola",
                shortDescription = "Formasi dasar (4-3-3, 4-4-2), aturan offside, pelanggaran, tendangan bebas, dan kartu.",
                learningObjectives = "1. Memahami perbedaan formasi 4-3-3, 4-4-2, dan 3-5-2.\n2. Mengidentifikasi posisi offside dan sanksi kartu kuning/merah.\n3. Memahami peran wasit dan asisten wasit.",
                contentBody = "Sepak bola dimainkan 11 vs 11 selama 2x45 menit. Memahami formasi menyerang dan bertahan serta aturan offside adalah kunci kecerdasan bermain (football IQ).",
                basicTechniques = "1. Formasi 4-3-3: Menekankan penguasaan bola lini tengah dan serangan sayap cepat.\n2. Formasi 4-4-2: Keseimbangan solid pertahanan dan duet striker.\n3. Aturan Offside: Pemain berada lebih dekat ke garis gawang lawan daripada bola dan pemain bertahan kedua terakhir lawan saat bola dioperkan.",
                commonMistakes = "1. Pemain penyerang terlambat mundur sehingga terperangkap jebakan offside.\n2. Gelandang tidak menjaga jarak antar lini sehingga mudah ditembus lawan.",
                tips = "Selalu perhatikan garis bek terakhir lawan sebelum meminta bola daerah.",
                practiceExercises = "Simulasi taktik transisi menyerang ke bertahan dalam game mini 5 vs 5.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=Fq_nE3WbZ0A",
                readTimeMinutes = 7,
                orderIndex = 5,
                isPopular = true
            )
        )

        // --- BOLA BASKET ---
        materials.add(
            MaterialEntity(
                id = 6,
                category = SportCategory.BOLA_BASKET,
                title = "Passing: Chest Pass, Bounce Pass & Overhead Pass",
                shortDescription = "Variasi operan dada, pantul, dan atas kepala untuk membongkar pertahanan lawan.",
                learningObjectives = "1. Melakukan chest pass kencang lurus setinggi dada rekan.\n2. Mengarahkan bounce pass 2/3 jarak menuju rekan.\n3. Melakukan overhead pass untuk melewati hadangan lawan tinggi.",
                contentBody = "Passing yang akurat dan cepat membedah zona pertahanan lawan. Tiga operan fundamental dalam bola basket adalah Chest Pass, Bounce Pass, dan Overhead Pass.",
                basicTechniques = "1. Chest Pass: Pegang bola di depan dada, dorong lurus ke depan dengan meluruskan siku dan mengibaskan pergelangan tangan (flick) ke luar.\n2. Bounce Pass: Pantulkan bola ke lantai sekitar 2/3 jarak dari teman penerima agar memantul nyaman ke pinggangnya.\n3. Overhead Pass: Tarik bola di atas kening (jangan terlalu belakang), lecutkan ke depan dengan kuat.",
                commonMistakes = "1. Menarik bola terlalu jauh ke belakang kepala saat overhead pass sehingga mudah direbut lawan dari belakang.\n2. Pantulan bounce pass terlalu dekat dengan penerima sehingga pantulan terlalu rendah.",
                tips = "Langkahkan satu kaki ke depan saat mengoper untuk menambah dorongan tenaga dan stabilitas.",
                practiceExercises = "Tiga orang passing segitiga rotasi cepat kombinasi 3 jenis passing selama 10 menit.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=Z_a7H56bTyk",
                readTimeMinutes = 6,
                orderIndex = 1,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 7,
                category = SportCategory.BOLA_BASKET,
                title = "Dribbling, Crossover & Pivot",
                shortDescription = "Kuasai dribble rendah, tinggi, crossover protektif, dan teknik memutar tubuh (pivot).",
                learningObjectives = "1. Melakukan dribble tanpa melihat bola menggunakan bantalan jari.\n2. Mempraktikkan pivot tanpa mengangkat kaki poros.\n3. Melakukan crossover cepat untuk merubah arah serangan.",
                contentBody = "Dribbling adalah cara menggerakkan bola secara mandiri. Menggabungkan dribble lincah dengan pivot yang kokoh menjaga bola dari sergapan bek lawan.",
                basicTechniques = "1. Dribble: Pantulkan bola menggunakan ruas jari (bukan telapak), tekuk lutut, badan condong, lindungi bola dengan lengan lainnya.\n2. Pivot: Tentukan satu kaki tumpu sebagai poros yang tidak boleh bergeser, putar badan ke segala arah untuk mencari ruang operan.",
                commonMistakes = "1. Memukul bola dengan telapak tangan (slapping).\n2. Menggeser kaki poros saat pivot yang berakibat pelanggaran Traveling.",
                tips = "Latih dribble dengan bola tenis di tangan satunya untuk melatih reflek sensorik tanpa melihat bola basket.",
                practiceExercises = "Dribble cone zig-zag tangan kanan dan kiri, lalu lakukan pivot 180 derajat sebelum passing.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=CqYQ1sU7_40",
                readTimeMinutes = 6,
                orderIndex = 2,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 8,
                category = SportCategory.BOLA_BASKET,
                title = "Shooting & Lay-up Shoot (Tembakan Melayang)",
                shortDescription = "Prinsip tembakan B.E.E.F (Balance, Eyes, Elbow, Follow-through) dan irama 2 langkah lay-up.",
                learningObjectives = "1. Menerapkan formula B.E.E.F pada set shoot dan jump shoot.\n2. Melakukan langkah 1-2 lay-up kanan dan kiri dengan pantulan papan kotak.\n3. Meningkatkan akurasi tembakan bebas (free throw).",
                contentBody = "Mencetak poin adalah tujuan utama. Shooting memerlukan mekanika tubuh yang konsisten, sedangkan lay-up adalah tembakan persentase masuk tertinggi saat mendekati ring.",
                basicTechniques = "1. Prinsip BEEF:\n  - B (Balance): Kaki sejajar bahu, lutut ditekuk.\n  - E (Eyes): Fokus pada target ring/papan.\n  - E (Elbow): Siku 90 derajat sejajar ring.\n  - F (Follow Through): Jari mengibaskan bola membentuk leher angsa (swan neck).\n2. Lay-up: Dribble mendekat, langkah panjang pertama kaki kanan, langkah pendek kedua kaki kiri lalu lompat tinggi meletakkan bola ke papan sudut atas.",
                commonMistakes = "1. Melompat ke depan bukan ke atas saat melakukan lay-up shoot.\n2. Tangan penyeimbang ikut mendorong bola saat shooting.",
                tips = "Pantulkan bola tepat pada sudut kotak kecil di papan pantul (backboard) untuk memastikan bola masuk ke jaring.",
                practiceExercises = "Latihan 20 tembakan lay-up sisi kanan dan 20 lay-up sisi kiri secara berurutan.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=Jb13yWwJv8c",
                readTimeMinutes = 7,
                orderIndex = 3,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 9,
                category = SportCategory.BOLA_BASKET,
                title = "Rebound & Teknik Pertahanan (Defense Stance)",
                shortDescription = "Box out merebut bola pantul dan stance bertahan man-to-man yang agresif.",
                learningObjectives = "1. Mempraktikkan posisi bertahan (defensive stance) dan slide step.\n2. Melakukan teknik box out mengunci posisi lawan saat rebound.\n3. Menguasai offensive dan defensive rebound.",
                contentBody = "'Defense wins championships'. Pertahanan yang solid dan penguasaan rebound menghentikan momentum lawan dan menciptakan peluang fast break.",
                basicTechniques = "1. Defensive Stance: Rendahkan pinggul, rentangkan kedua tangan lebar-lebar, bergerak dengan slide (jangan menyilangkan kaki).\n2. Box Out: Saat bola ditembakkan, putar badan membelakangi lawan, tempelkan punggung/pinggul untuk memblokir pergerakan lawan, lalu lompat rebut bola dengan dua tangan.",
                commonMistakes = "1. Berdiri tegak saat bertahan sehingga mudah dilewati dengan first step lawan.\n2. Terlalu fokus melihat bola tanpa melakukan box out pada pemain lawan di sekitarnya.",
                tips = "Amankan bola rebound di depan dada dengan kedua siku sedikit membuka untuk memproteksi bola.",
                practiceExercises = "Drill 1 on 1 defense slide dari baseline ke half-court dilanjutkan duel rebound.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=2r1pXW5q3lA",
                readTimeMinutes = 5,
                orderIndex = 4,
                isPopular = false
            )
        )
        materials.add(
            MaterialEntity(
                id = 10,
                category = SportCategory.BOLA_BASKET,
                title = "Peraturan Pertandingan, Waktu & Pelanggaran Basket",
                shortDescription = "Aturan 24/8/5/3 detik, foul pribadi, technical foul, dan sistem penilaian poin 1, 2, dan 3.",
                learningObjectives = "1. Memahami batasan waktu penyerangan 24 detik dan 8 detik pindah lapangan.\n2. Mengetahui perbedaan foul pribadi, offensive foul, dan technical foul.\n3. Mengetahui ukuran lapangan dan ketentuan substitusi pemain.",
                contentBody = "Pertandingan basket FIBA dimainkan 4 kuarter x 10 menit (NBA 4x12 menit). Pemain yang melakukan 5 kali personal foul akan dikeluarkan dari pertandingan (foul out).",
                basicTechniques = "1. Aturan Waktu:\n  - 24 Detik: Waktu shot clock untuk menembak mengenai ring.\n  - 8 Detik: Membawa bola dari backcourt ke frontcourt.\n  - 3 Detik: Penyerang dilarang berada di key area (restricted area) lebih dari 3 detik.\n2. Poin: Free throw (1 poin), Tembakan dalam busur (2 poin), Tembakan luar busur (3 poin).",
                commonMistakes = "1. Menggiring bola kembali setelah menghentikan dribble (Double Dribble).\n2. Melangkah lebih dari 2 langkah tanpa mendribble (Traveling).",
                tips = "Perhatikan hitungan detik wasit dan sinyal visual shot clock di atas papan ring.",
                practiceExercises = "Kuis simulasi situasi pertandingan dan aturan wasit bersama rekan kelas.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=Vp_g87E_zKw",
                readTimeMinutes = 6,
                orderIndex = 5,
                isPopular = false
            )
        )

        // --- BOLA VOLI ---
        materials.add(
            MaterialEntity(
                id = 11,
                category = SportCategory.BOLA_VOLI,
                title = "Passing Bawah dan Passing Atas (Dig & Set)",
                shortDescription = "Kunci penerimaan servis dan umpan toser yang lembut, stabil, dan presisi.",
                learningObjectives = "1. Membentuk kaitan tangan passing bawah yang datar dan kokoh.\n2. Melakukan passing atas menggunakan ruas jari berbentuk mangkuk di atas dahi.\n3. Mengarahkan bola ke posisi toser (setter).",
                contentBody = "Passing adalah napas dalam permainan bola voli. Passing bawah (dig/bump) digunakan untuk menerima servis keras dan smash lawan, sedangkan passing atas (set/overhead pass) digunakan untuk mengumpan spike.",
                basicTechniques = "1. Passing Bawah: Rapatkan kedua ibu jari sejajar, kunci siku lurus, ayunkan lengan dari bahu ke atas (maksimal 90 derajat), dorong dengan meluruskan lutut.\n2. Passing Atas: Buka jari-jari tangan membentuk mangkuk di atas dahi, dorong bola menggunakan ruas jari lentur dibantu pelurusan siku dan lutut.",
                commonMistakes = "1. Siku ditekuk saat melakukan passing bawah sehingga pantulan bola melenceng.\n2. Menangkap bola atau mendorong dengan telapak tangan pada passing atas (ball handling fault).",
                tips = "Arahkan bidang datar lengan bawah (antara pergelangan dan siku) tepat menghadap target umpan.",
                practiceExercises = "Latihan passing bawah mandiri ke atas sebanyak 50 kali tanpa jatuh.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=0kF4_7qT2tM",
                readTimeMinutes = 6,
                orderIndex = 1,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 12,
                category = SportCategory.BOLA_VOLI,
                title = "Servis Bawah dan Servis Atas (Underhand & Overhand Serve)",
                shortDescription = "Teknik mengawali reli dengan servis presisi hingga floating & jump serve mematikan.",
                learningObjectives = "1. Melakukan servis bawah stabil melewati net.\n2. Melakukan servis atas dengan pukulan telapak tangan terbuka.\n3. Menempatkan servis pada titik lemah zona lapangan lawan.",
                contentBody = "Servis bukan sekadar pukulan pembuka, melainkan serangan pertama. Servis yang tajam dapat langsung menghasilkan poin (Ace) atau menyulitkan lawan menyusun serangan.",
                basicTechniques = "1. Servis Bawah: Berdiri dengan kaki kiri di depan, pegang bola di tangan kiri setinggi pinggang, ayunkan tangan kanan dari belakang dengan kepalan tangan, pukul bagian bawah bola.\n2. Servis Atas: Lempar bola 1-1.5 meter di atas kepala, ayunkan tangan kanan dari belakang kepala, pukul bagian tengah bola dengan telapak tangan terbuka keras.",
                commonMistakes = "1. Menginjak garis belakang lapangan sebelum bola dipukul (foot fault).\n2. Lambungan bola servis atas terlalu jauh ke depan atau belakang.",
                tips = "Lakukan kontak bola pada titik tertinggi ayunan tangan untuk lintasan bola menukik.",
                practiceExercises = "Latihan 20 kali servis atas menargetkan cone di sudut lapangan lawan.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=a7b6C9D1w2k",
                readTimeMinutes = 6,
                orderIndex = 2,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 13,
                category = SportCategory.BOLA_VOLI,
                title = "Smash (Spike) dan Serangan Cepat (Quick Attack)",
                shortDescription = "Tahapan awalan 3 langkah, tolakan loncatan vertikal, cambukan tangan, dan pendaratan.",
                learningObjectives = "1. Melakukan ritme awalan smash (langkah lambat - langkah cepat - lompat).\n2. Memukul bola di titik puncak dengan gerakan mencambuk (whip action).\n3. Mendarat lentuk dengan kedua kaki untuk mencegah cedera lutut.",
                contentBody = "Smash adalah pukulan keras menukik ke bidang lapangan lawan untuk mematikan bola. Memerlukan timing tepat dengan umpan toser dan kekuatan lompatan eksplosif.",
                basicTechniques = "1. Awalan: 3-4 langkah mendekati net dari luar garis serang.\n2. Tolakan: Hentakkan kedua kaki bersamaan sambil mengayunkan kedua lengan ke atas.\n3. Pukulan: Lengkungkan punggung, ayunkan tangan dominan mencambuk bola di bagian atas-belakang bola.\n4. Pendaratan: Mendarat dengan kedua ujung telapak kaki secara seimbang, tekuk lutut untuk meredam beban.",
                commonMistakes = "1. Menyentuh net dengan bagian tubuh mana pun (net touch fault).\n2. Mendarat hanya pada satu kaki atau melewati garis tengah (centerline fault).",
                tips = "Fokus pada timing lompatan; lompatlah saat bola mulai turun dari puncak umpan toser.",
                practiceExercises = "Latihan loncatan spike tanpa bola di depan net 15 repetisi fokus pendaratan seimbang.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=3gA8aR2yM9s",
                readTimeMinutes = 7,
                orderIndex = 3,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 14,
                category = SportCategory.BOLA_VOLI,
                title = "Block (Bendungan) Tunggal & Ganda",
                shortDescription = "Membendung spike lawan di atas bibir net dengan posisi jari terbuka dan pergeseran cepat.",
                learningObjectives = "1. Menguasai langkah geser (side step) dan cross step pemain blocker.\n2. Melakukan tolakan vertikal lurus tanpa menyentuh net.\n3. Membuka jari-jari tangan condong menutupi sudut pukulan lawan.",
                contentBody = "Block adalah pertahanan baris depan pertama untuk membendung laju spike lawan atau memperlambat bola agar mudah diamankan pemain bertahan (digger/libero).",
                basicTechniques = "1. Siap Sedia: Berdiri 30 cm dari net, kedua tangan setinggi dada telapak tangan menghadap net.\n2. Lompatan: Melompat lurus vertikal sesaat setelah spiker lawan melompat.\n3. Penetrasi: Dorong kedua tangan menyeberangi atas net sedalam mungkin ke area lawan, buka jari-jari kokoh, tegangkan pergelangan tangan.",
                commonMistakes = "1. Melompat maju ke depan sehingga menabrak net atau kaki lawan.\n2. Menutup mata saat melakukan block sehingga bola luput dari hadangan tangan.",
                tips = "Jangan sentuh bola jika bola masih berada di area setter lawan sebelum melakukan serangan.",
                practiceExercises = "Latihan lateral slide blocking di sepanjang net 10 set bolak-balik.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=kYv98pL7kGg",
                readTimeMinutes = 5,
                orderIndex = 4,
                isPopular = false
            )
        )
        materials.add(
            MaterialEntity(
                id = 15,
                category = SportCategory.BOLA_VOLI,
                title = "Posisi Pemain, Rotasi & Sistem Pertahanan",
                shortDescription = "Posisi 1-6 searah jarum jam, peran Libero, Setter, Outside Hitter, dan Opposite.",
                learningObjectives = "1. Memahami urutan rotasi 6 posisi pemain saat memenangkan servis.\n2. Mengetahui peran khusus Libero dan aturan pergantiannya.\n3. Menerapkan pola formasi pertahanan W (5 penerima servis).",
                contentBody = "Setiap tim beranggotakan 6 pemain di lapangan. Ketika tim penerima servis merebut hak servis, seluruh pemain harus berotasi 1 posisi searah jarum jam (clockwise).",
                basicTechniques = "1. Posisi 1 (Belakang Kanan - Servis), Posisi 2 (Depan Kanan), Posisi 3 (Depan Tengah), Posisi 4 (Depan Kiri), Posisi 5 (Belakang Kiri), Posisi 6 (Belakang Tengah).\n2. Peran Pemain:\n  - Setter/Tosser: Pengatur serangan.\n  - Spiker/Hitter: Pencetak poin serangan.\n  - Libero: Spesialis bertahan berkaus beda warna, tidak boleh servis, smash, atau block.",
                commonMistakes = "1. Melakukan kesalahan posisi (rotational fault) sebelum servis dipukul.\n2. Libero melakukan passing atas di area serang yang langsung di-smash rekan.",
                tips = "Selalu perhatikan pemain depan dan samping Anda agar tidak melanggar overlap posisi rotasi.",
                practiceExercises = "Simulasi rotasi 6 pemain dalam papan taktik dan implementasi di lapangan.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=Fq_nE3WbZ0A",
                readTimeMinutes = 6,
                orderIndex = 5,
                isPopular = false
            )
        )

        // --- ATLETIK ---
        materials.add(
            MaterialEntity(
                id = 16,
                category = SportCategory.ATLETIK,
                title = "Lari Jarak Pendek (Sprint 100m, 200m, 400m)",
                shortDescription = "Kecepatan maksimal lari cepat, fase dorongan kaki, ayunan lengan, dan frekuensi langkah.",
                learningObjectives = "1. Memaksimalkan fase akselerasi dan top speed sprint.\n2. Menjaga postur tubuh condong aerodinamis dan rileks.\n3. Memahami sistem pembagian lintasan dan finish lari sprint.",
                contentBody = "Lari jarak pendek (sprint) menuntut kecepatan maksimal sepanjang lintasan. Komponen utamanya adalah daya ledak otot (power), frekuensi langkah, dan koordinasi neuromuskular.",
                basicTechniques = "1. Fase Akselerasi (0-30m): Badan condong ke depan 45 derajat, dorongan kaki kuat ke belakang.\n2. Fase Top Speed (30-80m): Badan tegak rileks, angkat paha tinggi (high knee), ayunkan lengan 90 derajat dari bahu.\n3. Fase Finish: Condongkan dada ke depan saat menyentuh garis pita finish.",
                commonMistakes = "1. Mengangkat badan terlalu cepat sesaat setelah start.\n2. Menoleh ke kanan/kiri saat berlari kencang yang mengurangi kecepatan.",
                tips = "Jaga otot wajah, rahang, dan bahu tetap rileks agar energi fokus pada dorongan tungkai kaki.",
                practiceExercises = "Latihan sprint interval 30 meter x 6 repetisi dengan istirahat penuh 2 menit.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=0kF4_7qT2tM",
                readTimeMinutes = 6,
                orderIndex = 1,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 17,
                category = SportCategory.ATLETIK,
                title = "Teknik Start Jongkok (Crouch Start)",
                shortDescription = "Aba-aba 'Bersedia', 'Siap', 'Ya' (Bunyi Pistol), penempatan starting block, dan sudut lutut.",
                learningObjectives = "1. Memasang starting block dengan jarak yang presisi sesuai panjang kaki.\n2. Melakukan posisi 'Bersedia' dan 'Siap' dengan sudut lutut optimal.\n3. Meledak keluar dari balok start pada aba-aba 'Ya'.",
                contentBody = "Start jongkok digunakan khusus untuk nomor lari cepat (100m, 200m, 400m, lari gawang, dan pelari pertama estafet). Terdapat 3 jenis start: Bunch Start (pendek), Medium Start (sedang), dan Elongated Start (panjang).",
                basicTechniques = "1. 'Bersedia': Letakkan kedua tangan di belakang garis start berbentuk huruf V terbalik, lutut kaki belakang menyentuh tanah sejajar telapak kaki depan.\n2. 'Siap': Angkat pinggul lebih tinggi sedikit dari bahu, condongkan berat badan ke tangan, sudut lutut depan 90 derajat dan belakang 120 derajat.\n3. 'Ya'/Pistol: Tolakkan kedua kaki kuat pada balok, dorong tangan ke depan, langkah pertama cepat dan bertenaga.",
                commonMistakes = "1. Mengangkat kepala mendongak ke depan pada posisi 'Siap' yang membuat leher tegang.\n2. Mencuri start (false start) yang berakibat diskualifikasi langsung.",
                tips = "Fokuskan pendengaran penuh pada bunyi pistol tembakan, jangan menebak-nebak waktu tembakan.",
                practiceExercises = "Latihan reaksi start 10 meter sebanyak 8 repetisi.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=1oW_W1N_Qc8",
                readTimeMinutes = 6,
                orderIndex = 2,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 18,
                category = SportCategory.ATLETIK,
                title = "Lari Estafet (Relay 4x100m & 4x400m)",
                shortDescription = "Pergantian tongkat estafet cara visual (melihat) dan non-visual (tanpa melihat) di wissel zone.",
                learningObjectives = "1. Melakukan teknik perpindahan tongkat non-visual untuk lari 4x100m.\n2. Melakukan teknik visual untuk lari 4x400m.\n3. Memahami aturan zona pergantian tongkat (passing zone 20-30 meter).",
                contentBody = "Lari estafet adalah satu-satunya nomor lari beregu di cabang atletik. Kunci kemenangan bukan hanya kecepatan individu, melainkan kelancaran pergantian tongkat estafet tanpa mengurangi kecepatan lari.",
                basicTechniques = "1. Non-Visual (4x100m): Penerima tongkat mulai berlari saat pelari pembawa mencapai tanda cek (check mark), julurkan tangan ke belakang telapak menghadap atas tanpa menoleh.\n2. Visual (4x400m): Penerima menolehkan kepala melihat tongkat dan menggapainya dengan aman.\n3. Teknik Pemberian: Up-sweep (dari bawah ke atas) atau Down-sweep (dari atas ke bawah telapak tangan).",
                commonMistakes = "1. Menjatuhkan tongkat estafet saat proses penyerahan.\n2. Melakukan pergantian tongkat di luar batas wissel zone (diskualifikasi).",
                tips = "Gunakan strategi penempatan: Pelari 1 (start terbaik), Pelari 2 (lintasan lurus terkuat), Pelari 3 (ahli tikungan), Pelari 4 (finisher bermental baja).",
                practiceExercises = "Latihan passing tongkat berpasangan pada kecepatan penuh 6 repetisi.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=Z_a7H56bTyk",
                readTimeMinutes = 7,
                orderIndex = 3,
                isPopular = false
            )
        )
        materials.add(
            MaterialEntity(
                id = 19,
                category = SportCategory.ATLETIK,
                title = "Lari Jarak Menengah (Middle Distance 800m & 1500m)",
                shortDescription = "Pengaturan pace daya tahan aerobik-anaerobik, pernapasan ritmis, dan start berdiri.",
                learningObjectives = "1. Menerapkan teknik start berdiri (standing start).\n2. Mengatur ritme lari (pacing strategy) agar tidak cepat kehabisan tenaga.\n3. Menguasai pernapasan kombinasi hidung dan mulut yang efisien.",
                contentBody = "Lari jarak menengah mengombinasikan daya tahan kardiorespirasi tinggi dengan kecepatan lari taktis. Atlet menggunakan start berdiri dan berlari memotong ke lintasan terdalam (lintasan 1) setelah breakline.",
                basicTechniques = "1. Start Berdiri: Kaki tumpu terkuat di depan tepat di belakang garis lengkung start, badan condong, lutut sedikit ditekuk.\n2. Langkah Lari: Pendaratan telapak kaki bagian tengah (midfoot strike), sudut ayunan lengan rileks.\n3. Taktik Lari: Pertahankan pace konsisten pada 60% awal jarak, lalu lakukan negative split dan kick sprint 200m terakhir.",
                commonMistakes = "1. Langsung sprint habis-habisan di lap pertama sehingga mengalami penumpukan asam laktat dini.\n2. Bernapas terlalu pendek dan dangkal.",
                tips = "Latih pola napas 2-2 (dua langkah hirup napas, dua langkah hembuskan napas).",
                practiceExercises = "Latihan lari interval 400 meter x 4 repetisi dengan target waktu konstan.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=CqYQ1sU7_40",
                readTimeMinutes = 6,
                orderIndex = 4,
                isPopular = false
            )
        )
        materials.add(
            MaterialEntity(
                id = 20,
                category = SportCategory.ATLETIK,
                title = "Lompat Jauh (Long Jump: Awalan, Tolakan, Melayang, Mendarat)",
                shortDescription = "Kuasai gaya jongkok (tuck), gaya menggantung (hang), dan berjalan di udara (walking in the air).",
                learningObjectives = "1. Mengembangkan kecepatan lari awalan yang terkontrol dan konsisten.\n2. Melakukan tolakan satu kaki kuat tepat di atas papan tumpu (take-off board).\n3. Mendarat di bak pasir dengan kedua kaki mengeper condong ke depan.",
                contentBody = "Lompat jauh bertujuan mencapai jarak lompatan sejauh-jauhnya. Terdiri dari 4 fase berurutan: Awalan (Approach Run), Tolakan (Take-off), Melayang di Udara (Flight), dan Mendarat (Landing).",
                basicTechniques = "1. Awalan: Lari 30-40 meter dengan kecepatan bertahap mencapai top speed di 4 langkah terakhir.\n2. Tolakan: Hentakkan satu kaki terkuat di papan tumpu (jangan melewati garis plastisin), ayunkan lutut kaki bebas dan kedua tangan ke atas.\n3. Melayang: Pilih gaya menggantung (lentikkan punggung) atau gaya berjalan di udara.\n4. Mendarat: Julurkan kedua kaki lurus ke depan bak pasir, tekuk lutut saat menyentuh pasir, dorong badan ke depan.",
                commonMistakes = "1. Kaki melebihi papan tumpu sehingga lompatan dinyatakan Diskualifikasi (Foul/No Jump).\n2. Jatuh ke belakang dengan tangan menopang di pasir di belakang tumit (jarak dihitung dari bekas terbelakang).",
                tips = "Ukur langkah awalan secara presisi menggunakan meteran agar titik tumpu selalu konsisten.",
                practiceExercises = "Latihan lompat dari box pendek ke bak pasir fokus ayunan kaki mendarat 10 kali.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=Jb13yWwJv8c",
                readTimeMinutes = 7,
                orderIndex = 5,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 21,
                category = SportCategory.ATLETIK,
                title = "Lompat Tinggi (High Jump: Gaya Flop & Straddle)",
                shortDescription = "Lengkungan tubuh melewati mistar dengan gaya Fosbury Flop dan teknik awalan kurva J.",
                learningObjectives = "1. Memahami lintasan lari awalan berbentuk kurva huruf J.\n2. Melakukan tolakan vertikal membelakangi mistar.\n3. Melengkungkan punggung (arch) di atas mistar dan mendarat di matras busa.",
                contentBody = "Lompat tinggi menguji kelenturan dan lompatan vertikal atlet melewati mistar tanpa menjatuhkannya. Gaya Fosbury Flop adalah gaya paling dominan di dunia atletik modern.",
                basicTechniques = "1. Awalan Kurva J: Lari lurus 4-5 langkah lalu berbelok melengkung 4-5 langkah terakhir untuk menciptakan gaya sentrifugal.\n2. Tolakan: Tolak dengan kaki luar mistar, ayunkan lutut dalam ke atas, putar bahu membelakangi mistar.\n3. Melayang: Lengkungkan pinggang ke atas membentuk jembatan di atas mistar, setelah pinggul lewat segera tarik dagu ke dada dan tendang kaki lurus ke atas agar tidak menyenggol mistar.\n4. Mendarat: Mendarat dengan punggung atas di atas matras tebal.",
                commonMistakes = "1. Menabrak mistar saat bertolak karena jarak tolakan terlalu dekat.\n2. Tidak melengkungkan pinggang sehingga pantat menyenggol mistar.",
                tips = "Latih kekuatan core dan fleksibilitas tulang belakang dengan jembatan kayang.",
                practiceExercises = "Drill tolakan vertikal satu kaki membelakangi mistar rendah 10 kali.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=2r1pXW5q3lA",
                readTimeMinutes = 6,
                orderIndex = 6,
                isPopular = false
            )
        )
        materials.add(
            MaterialEntity(
                id = 22,
                category = SportCategory.ATLETIK,
                title = "Tolak Peluru (Shot Put: Gaya Ortodoks & O'Brien)",
                shortDescription = "Teknik menolak peluru besi (bukan melempar) dengan gaya menyamping dan membelakangi.",
                learningObjectives = "1. Memegang peluru rapat di bawah pangkal rahang telinga.\n2. Melakukan geseran kaki (glide) gaya O'Brien dari belakang lingkaran tolak.\n3. Melakukan tolakan eksplosif dengan sudut pelepasan 40-45 derajat.",
                contentBody = "Tolak peluru adalah nomor lempar yang menggunakan gerakan mendorong/menolak peluru logam bulat berat (3-5 kg untuk pelajar). Dilarang melempar peluru seperti bola bisbol karena membahayakan persendian bahu.",
                basicTechniques = "1. Pegangan: Letakkan peluru pada pangkal jari-jari tangan, tempelkan erat pada leher di bawah rahang telinga kanan.\n2. Sikap Awal (O'Brien): Berdiri membelakangi sektor lemparan, bungkukkan badan ke depan dengan bertumpu pada kaki kanan.\n3. Luncuran (Glide): Hentakkan kaki kiri ke belakang meluncur melintasi lingkaran tolak.\n4. Tolakan: Putar pinggul ke depan, dorong peluru dengan kekuatan tungkai, pinggang, dan lengan secara simultan hingga lengan lurus.",
                commonMistakes = "1. Peluru terlepas dari leher sebelum ditolak (gerakan melempar = diskualifikasi).\n2. Menginjak atau keluar melewati balok penahan tolak peluru bagian depan.",
                tips = "Manfaatkan putaran pinggul dan daya dorong tungkai bawah, bukan hanya mengandalkan tenaga lengan.",
                practiceExercises = "Latihan dorongan bola medicine 3 kg dari leher 15 repetisi.",
                safetyWarning = "Pastikan area sektor lemparan bebas dari orang sebelum melakukan tolakan.",
                videoUrl = "https://www.youtube.com/watch?v=Vp_g87E_zKw",
                readTimeMinutes = 6,
                orderIndex = 7,
                isPopular = false
            )
        )
        materials.add(
            MaterialEntity(
                id = 23,
                category = SportCategory.ATLETIK,
                title = "Lempar Lembing & Lempar Cakram",
                shortDescription = "Teknik pegangan Amerika/Finlandia pada lembing dan rotasi cakram dalam sektor lempar.",
                learningObjectives = "1. Memahami 3 gaya pegangan lembing (American, Finnish, 'V' Grip).\n2. Melakukan langkah silang (cross step) saat awalan lempar lembing.\n3. Memahami teknik pegangan dan putaran cakram dari ujung jari.",
                contentBody = "Lempar lembing dan cakram memerlukan koordinasi kelenturan, kecepatan awalan, serta sudut pelepasan aerodinamis agar alat melayang jauh dan menancap sah di sektor lapangan.",
                basicTechniques = "1. Lempar Lembing: Pegang tali lilitan lembing di samping telinga, lakukan lari awalan dilanjutkan 4 langkah berirama silang (cross step), tarik lembing ke belakang penuh, lemparkan melewati atas bahu.\n2. Lempar Cakram: Pegang cakram pada ruas jari terakhir, ayunkan lengan ke belakang, putar badan dari posisi menyamping atau berputar 1,5 putaran, lepaskan cakram berputar searah jarum jam dari jari telunjuk.",
                commonMistakes = "1. Ujung mata lembing tidak menancap di tanah (flat landing).\n2. Cakram dilepaskan tanpa putaran spin sehingga melayang tidak stabil.",
                tips = "Lembing harus selalu dibawa dalam posisi tegak saat menuju lapangan latihan demi keselamatan bersama.",
                practiceExercises = "Latihan ayunan lempar bola kasti berbobot untuk membiasakan poros bahu.",
                safetyWarning = "Patuhi protokol keselamatan ketat: Jangan pernah mengambil lembing saat ada pelempar lain bersiap.",
                videoUrl = "https://www.youtube.com/watch?v=a7b6C9D1w2k",
                readTimeMinutes = 7,
                orderIndex = 8,
                isPopular = false
            )
        )
        materials.add(
            MaterialEntity(
                id = 24,
                category = SportCategory.ATLETIK,
                title = "Jalan Cepat (Race Walking: Teknik & Peraturan)",
                shortDescription = "Perbedaan jalan dan lari, kontak kaki tanpa putus dengan tanah, dan lutut lurus saat tumpuan.",
                learningObjectives = "1. Memahami definisi jalan cepat: selalu ada kontak dengan tanah (no loss of contact).\n2. Meluruskan kaki tumpuan saat posisi tegak lurus tubuh.\n3. Menguasai ayunan pinggul dinamis tanpa mengangkat bahu.",
                contentBody = "Jalan cepat adalah gerak maju langkah kaki yang dilakukan sedemikian rupa sehingga kontak dengan tanah terpelihara tanpa terputus. Kaki depan harus diluruskan (tidak ditekuk pada lutut) sejak kontak pertama hingga posisi vertikal.",
                basicTechniques = "1. Kontak Kaki: Tumit kaki depan menyentuh tanah sebelum jari kaki belakang meninggalkan tanah.\n2. Lutut: Kaki penumpu harus lurus sempurna tanpa tekukan lutut sesaat saat melewati badan.\n3. Pinggul: Gerakkan pinggul memutar lentur ke depan untuk memperlebar jangkauan langkah.\n4. Postur & Lengan: Badan tegak, tekuk siku 90 derajat, ayunkan tangan ritmis setinggi dada.",
                commonMistakes = "1. Melayang di udara (keduanya terangkat dari tanah = kartu merah pelanggaran 'Lifting').\n2. Lutut tetap ditekuk saat tumpuan kaki (pelanggaran 'Bent Knee').",
                tips = "Fokus pada kelenturan sendi panggul dan frekuensi langkah cepat daripada memaksakan langkah terlalu lebar.",
                practiceExercises = "Latihan jalan cepat lurus di atas garis lintasan 100 meter x 4 repetisi menjaga kontak tanah.",
                safetyWarning = "",
                videoUrl = "https://www.youtube.com/watch?v=3gA8aR2yM9s",
                readTimeMinutes = 6,
                orderIndex = 9,
                isPopular = false
            )
        )

        // --- PENANGANAN CEDERA ---
        materials.add(
            MaterialEntity(
                id = 25,
                category = SportCategory.PENANGANAN_CEDERA,
                title = "Pencegahan Cedera & Protokol Pemanasan / Pendinginan",
                shortDescription = "Pentingnya dynamic warm-up, stretching, hidrasi, dan cool-down untuk menghindari cedera fatal.",
                learningObjectives = "1. Membedakan pemanasan dinamis sebelum olahraga dan peregangan statis setelah olahraga.\n2. Memahami pentingnya hidrasi dan pengenalan batas kemampuan tubuh.\n3. Menyusun rutinitas pemanasan 10 menit yang komprehensif.",
                contentBody = "Cedera olahraga sebagian besar dapat dicegah dengan persiapan matang. Pemanasan menaikkan suhu inti tubuh, meningkatkan elastisitas otot, dan melumasi persendian dengan cairan sinovial.",
                basicTechniques = "1. Pemanasan Dinamis (5-10 menit): Jogging ringan, arm circles, lunges, leg swings, high knees.\n2. Perlengkapan Tepat: Gunakan sepatu olahraga berukuran pas dan pelindung (shin guard, knee pad jika diperlukan).\n3. Pendinginan (Cool-down 5 menit): Jalan santai dan peregangan statis menahan otot 15-20 detik untuk mengurai asam laktat.",
                commonMistakes = "1. Langsung melakukan olahraga intensitas tinggi tanpa pemanasan sedikit pun.\n2. Peregangan statis ekstrem dengan membal-bal (ballistic stretching) pada otot yang masih dingin.",
                tips = "Minum air 200-300 ml sekitar 20 menit sebelum berolahraga untuk menjaga hidrasi seluler.",
                practiceExercises = "Praktekkan protokol 5 menit dynamic warm up sebelum setiap sesi latihan PJOK.",
                safetyWarning = "PENTING: Materi ini adalah panduan edukasi dasar PJOK dan bukan pengganti diagnosis medis profesional.",
                videoUrl = "https://www.youtube.com/watch?v=kYv98pL7kGg",
                readTimeMinutes = 5,
                orderIndex = 1,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 26,
                category = SportCategory.PENANGANAN_CEDERA,
                title = "Pertolongan Pertama Prinsip R.I.C.E / P.R.I.C.E",
                shortDescription = "Protokol standar internasional: Protect, Rest, Ice, Compression, Elevation untuk cedera akut.",
                learningObjectives = "1. Memahami urutan tindakan P.R.I.C.E dalam 24-48 jam pertama cedera.\n2. Menerapkan kompres es secara aman tanpa merusak jaringan kulit.\n3. Memahami posisi elevasi yang benar di atas ketinggian jantung.",
                contentBody = "Prinsip PRICE adalah pertolongan pertama paling efektif untuk cedera jaringan lunak (otot, ligamen, tendon). Tujuannya mengurangi pendarahan internal, membatasi pembengkakan, dan meredakan nyeri akut.",
                basicTechniques = "1. P (Protect): Lindungi bagian yang cedera dari benturan lanjutan (gunakan bidai/splint jika perlu).\n2. R (Rest): Istirahatkan dan hentikan segera seluruh aktivitas olahraga.\n3. I (Ice): Beri kompres es yang dibalut kain selama 15-20 menit setiap 2-3 jam (JANGAN tempelkan es batu langsung ke kulit!).\n4. C (Compression): Balut dengan perban elastis (tensocrepe) dari arah bawah ke atas, jangan terlalu ketat hingga menghambat sirkulasi darah.\n5. E (Elevation): Posisikan bagian yang cedera lebih tinggi dari ketinggian jantung saat berbaring.",
                commonMistakes = "1. Memberikan kompres panas atau balsem hangat pada fase akut (dilarang dalam 48 jam pertama karena memperparah radang).\n2. Memijat (massage) langsung bagian yang bengkak/keseleo.",
                tips = "Ingat pantangan 'HARM' dalam 48 jam pertama: Hindari Heat (panas), Alcohol, Running (olahraga), dan Massage (pijat).",
                practiceExercises = "Simulasi membalut perban elastis pada pergelangan kaki secara berpasangan.",
                safetyWarning = "PERINGATAN KESELAMATAN: Segera bawa ke IGD/dokter spesialis jika terdapat deformitas bentuk tulang, mati rasa, atau nyeri tak tertahankan.",
                videoUrl = "https://www.youtube.com/watch?v=Fq_nE3WbZ0A",
                readTimeMinutes = 7,
                orderIndex = 2,
                isPopular = true
            )
        )
        materials.add(
            MaterialEntity(
                id = 27,
                category = SportCategory.PENANGANAN_CEDERA,
                title = "Penanganan Kram Otot Akut (Muscle Cramp)",
                shortDescription = "Penyebab dehidrasi elektrolit dan langkah peregangan lembut untuk meredakan kram betis/paha.",
                learningObjectives = "1. Mengidentifikasi tanda dan pemicu utama kram otot saat berolahraga.\n2. Melakukan peregangan pasif terarah untuk meregangkan otot yang berkontraksi hebat.\n3. Memberikan asupan cairan elektrolit yang tepat.",
                contentBody = "Kram otot adalah kontraksi otot yang kuat, tiba-tiba, dan tanpa sadar yang menimbulkan nyeri hebat. Sering terjadi pada otot betis (gastrocnemius), paha depan (quadriceps), dan paha belakang (hamstring).",
                basicTechniques = "1. Hentikan Aktivitas: Dudukkan atau baringkan penderita di tempat teduh dan nyaman.\n2. Peregangan Lembut (Stretching): Untuk kram betis, luruskan lutut dan dorong telapak kaki perlahan ke arah tulang kering (dorsofleksi).\n3. Pijatan Ringan: Usap perlahan ke arah jantung setelah otot mulai rileks.\n4. Rehidrasi: Berikan minuman isotonik atau air mineral dengan sedikit garam untuk memulihkan elektrolit natrium-kalium.",
                commonMistakes = "1. Menarik kaki dengan sentakan keras tiba-tiba yang dapat merobek serat otot.\n2. Membiarkan atlet langsung kembali bermain sebelum otot benar-benar rileks.",
                tips = "Lakukan peregangan betis secara rutin sebelum bertanding, terutama saat cuaca panas terik.",
                practiceExercises = "Praktekkan pertolongan kram betis mandiri dan berbantuan.",
                safetyWarning = "Edukasi pertolongan pertama dasar sekolah. Jika kram disertai demam tinggi atau tidak membaik, hubungi tim medis.",
                videoUrl = "https://www.youtube.com/watch?v=Z_a7H56bTyk",
                readTimeMinutes = 5,
                orderIndex = 3,
                isPopular = false
            )
        )
        materials.add(
            MaterialEntity(
                id = 28,
                category = SportCategory.PENANGANAN_CEDERA,
                title = "Keseleo (Sprain Ligamen) & Cedera Otot (Strain)",
                shortDescription = "Perbedaan robekan ligamen sendi dan otot, derajat keparahan (Grade 1, 2, 3), serta penanganannya.",
                learningObjectives = "1. Membedakan antara Sprain (cedera ligamen sendi) dan Strain (cedera otot/tendon).\n2. Mengenali tingkatan cedera ringan, sedang, dan berat (robek total).\n3. Melakukan imobilisasi sendi yang cedera.",
                contentBody = "Sprain terjadi saat ligamen yang menghubungkan tulang di persendian teregang atau robek (sering pada pergelangan kaki/ankle). Strain terjadi saat otot atau tendon tertarik berlebih (misal hamstring strain).",
                basicTechniques = "1. Grade 1 (Ringan): Peregangan serat minor, lakukan PRICE selama 3-5 hari.\n2. Grade 2 (Sedang): Robekan parsial, bengkak nyata, gunakan ankle support dan hindari menumpu berat badan.\n3. Grade 3 (Berat): Robekan total ligamen, sendi tidak stabil, butuh evaluasi ortopedi dan imobilisasi gips/operasi.",
                commonMistakes = "1. Memaksa berjalan normal saat ankle mengalami pembengkakan parah.\n2. Melakukan manipulasi sendi (mengurut/membunyikan sendi) yang bisa memperparah robekan ligamen.",
                tips = "Gunakan alas kaki dengan ankle support yang kokoh dan perhatikan permukaan lapangan yang berlubang.",
                practiceExercises = "Latihan propriosepsi keseimbangan berdiri satu kaki untuk rehabilitasi ankle ringan.",
                safetyWarning = "PERINGATAN: Periksakan ke fasilitas kesehatan jika terdengar bunyi 'pop' saat cedera atau sendi tidak dapat digerakkan.",
                videoUrl = "https://www.youtube.com/watch?v=1oW_W1N_Qc8",
                readTimeMinutes = 6,
                orderIndex = 4,
                isPopular = false
            )
        )
        materials.add(
            MaterialEntity(
                id = 29,
                category = SportCategory.PENANGANAN_CEDERA,
                title = "Penanganan Memar, Luka Lecet & Kapan Mencari Medis",
                shortDescription = "Pembersihan luka lecet steril, pertolongan hematoma/memar, dan red flag bahaya medis.",
                learningObjectives = "1. Membersihkan luka lecet dengan cairan antiseptik/NaCl steril tanpa alkohol perih.\n2. Memberikan kompres dingin pada memar (hematoma).\n3. Mengidentifikasi tanda darurat (Red Flags) yang mewajibkan rujukan dokter segera.",
                contentBody = "Luka lecet (ekskoriasi) dan memar (kontusio) adalah cedera permukaan paling sering akibat gesekan lapangan dan benturan tubuh. Penanganan higienis mencegah infeksi bakteri seperti tetanus.",
                basicTechniques = "1. Luka Lecet: Cuci tangan dengan sabun, bersihkan kotoran/pasir di luka dengan air mengalir atau NaCl steril, oleskan antiseptik povidone iodine, tutup dengan kassa steril tipis berpori.\n2. Memar: Kompres dingin 15 menit untuk menghentikan rembesan darah kapiler.\n3. RED FLAGS MEDIS (Segera bawa ke RS):\n  - Hilang kesadaran / pingsan / gegar otak (concussion).\n  - Perdarahan memancar hebat yang tidak berhenti setelah ditekan 10 menit.\n  - Deformitas tulang (tulang tampak bengkok/patah).\n  - Sesak napas hebat atau nyeri dada mendadak.",
                commonMistakes = "1. Meniup luka lecet yang justru menyebarkan kuman dari mulut ke luka.\n2. Mengoleskan pasta gigi atau minyak tanah pada luka bakar/lecet.",
                tips = "Kotak P3K sekolah harus selalu terisi lengkap dengan kassa steril, perban elastis, antiseptik, plester, dan ice pack siap pakai.",
                practiceExercises = "Praktek membersihkan dan membalut luka steril pada manekin/teman sekelas.",
                safetyWarning = "EDUKASI DASAR PJOK: Bukan pengganti tindakan medis darurat. Selalu hubungi nomor gawat darurat (119/112) untuk kasus kritis.",
                videoUrl = "https://www.youtube.com/watch?v=0kF4_7qT2tM",
                readTimeMinutes = 6,
                orderIndex = 5,
                isPopular = false
            )
        )

        database.materialDao().insertMaterials(materials)

        // 4. Seed Videos
        val videos = listOf(
            VideoEntity(
                1, SportCategory.SEPAK_BOLA,
                "Tutorial Passing & First Touch Sepak Bola", "08:45",
                "Panduan lengkap teknik passing kaki bagian dalam, luar, dan tips first touch tenang di bawah tekanan lawan.",
                "https://www.youtube.com/watch?v=0kF4_7qT2tM", "", 1
            ),
            VideoEntity(
                2, SportCategory.SEPAK_BOLA,
                "Mastering Dribbling & Finishing Gawang", "11:20",
                "Latihan dribble zig-zag cone dan teknik menembak akurat placing ke sudut gawang kiper.",
                "https://www.youtube.com/watch?v=1oW_W1N_Qc8", "", 2
            ),
            VideoEntity(
                3, SportCategory.BOLA_BASKET,
                "Panduan Chest Pass & Bounce Pass Basket", "07:30",
                "Teknik mengoper cepat dan akurat untuk membuka pertahanan lawan dalam skema permainan basket modern.",
                "https://www.youtube.com/watch?v=Z_a7H56bTyk", "", 1
            ),
            VideoEntity(
                4, SportCategory.BOLA_BASKET,
                "Mekanika Shooting B.E.E.F & Lay-up Kanan Kiri", "09:55",
                "Cara melatih form shooting yang konsisten dan irama 2 langkah layup berkecepatan tinggi.",
                "https://www.youtube.com/watch?v=Jb13yWwJv8c", "", 2
            ),
            VideoEntity(
                5, SportCategory.BOLA_VOLI,
                "Teknik Passing Bawah & Atas Voli Sempurna", "10:15",
                "Langkah pembentukan platform lengan passing bawah dan sentuhan ruas jari passing atas untuk toser.",
                "https://www.youtube.com/watch?v=0kF4_7qT2tM", "", 1
            ),
            VideoEntity(
                6, SportCategory.BOLA_VOLI,
                "Tutorial Awalan 3 Langkah Smash & Servis Atas", "12:40",
                "Langkah loncatan spike yang meledak di atas net dan teknik memukul servis atas tajam berputar.",
                "https://www.youtube.com/watch?v=3gA8aR2yM9s", "", 2
            ),
            VideoEntity(
                7, SportCategory.ATLETIK,
                "Teknik Start Jongkok & Akselerasi Sprint 100m", "08:10",
                "Aba-aba start jongkok, sudut balok tumpu, dan dorongan tenaga fase akselerasi lari cepat.",
                "https://www.youtube.com/watch?v=1oW_W1N_Qc8", "", 1
            ),
            VideoEntity(
                8, SportCategory.ATLETIK,
                "Fase Lompat Jauh & Tolakan Papan Tumpu", "09:15",
                "Analisis gerakan awalan kecepatan, tumpuan satu kaki, melayang gaya lentik, dan pendaratan bak pasir.",
                "https://www.youtube.com/watch?v=Jb13yWwJv8c", "", 2
            ),
            VideoEntity(
                9, SportCategory.PENANGANAN_CEDERA,
                "Panduan Lengkap Pertolongan Pertama Prinsip PRICE", "13:05",
                "Langkah proteksi, istirahat, kompres es kain, balut tekan elastis, dan elevasi kaki saat ankle sprain.",
                "https://www.youtube.com/watch?v=Fq_nE3WbZ0A", "", 1
            ),
            VideoEntity(
                10, SportCategory.PENANGANAN_CEDERA,
                "Penanganan Cepat Kram Betis & Pembersihan Luka", "07:45",
                "Peregangan dorsofleksi betis kram akut dan teknik desinfeksi luka lecet steril bebas perih.",
                "https://www.youtube.com/watch?v=kYv98pL7kGg", "", 2
            )
        )
        database.videoDao().insertVideos(videos)

        // 5. Seed Quizzes (Minimal 10 soal per kategori = 50 total questions)
        val quizzes = mutableListOf<QuizQuestionEntity>()

        // -- Sepak Bola Quizzes --
        quizzes.add(QuizQuestionEntity(0, 1, SportCategory.SEPAK_BOLA, "Bagian kaki yang paling tepat digunakan untuk mengoper bola jarak pendek dengan akurasi tinggi adalah...", "Ujung jari kaki", "Kaki bagian dalam", "Tumit kaki", "Punggung telapak kaki luar", 1, "Kaki bagian dalam memiliki bidang kontak yang luas dan datar sehingga operan sangat presisi."))
        quizzes.add(QuizQuestionEntity(0, 1, SportCategory.SEPAK_BOLA, "Pada saat melakukan passing kaki bagian dalam, posisi kaki tumpu yang benar berada di...", "Tepat di depan bola 50 cm", "Di belakang bola 1 meter", "Di samping bola berjarak sekitar 10-15 cm", "Menyilang di belakang kaki penendang", 2, "Kaki tumpu diletakkan di samping bola menghadap ke arah target operan."))
        quizzes.add(QuizQuestionEntity(0, 2, SportCategory.SEPAK_BOLA, "Agar bola hasil shooting melesat keras menyusur tanah atau datar ke sudut gawang, posisi badan penendang harus...", "Dicondongkan ke belakang", "Dicondongkan ke depan menutupi bola", "Miring ke kanan 90 derajat", "Melompat ke belakang", 1, "Condong ke depan menahan bola agar tidak melambung ke atas mistar gawang."))
        quizzes.add(QuizQuestionEntity(0, 3, SportCategory.SEPAK_BOLA, "Bagian kepala yang paling aman dan tepat digunakan untuk menyundul bola adalah...", "Ubun-ubun atas", "Tulang dahi (forehead)", "Pelipis samping", "Kepala bagian belakang", 1, "Tulang dahi adalah bagian terkuat dari tengkorak dan memungkinkan mata tetap melihat bola."))
        quizzes.add(QuizQuestionEntity(0, 3, SportCategory.SEPAK_BOLA, "Ketentuan lemparan ke dalam (throw-in) yang sah menurut peraturan sepak bola adalah...", "Dilempar dengan satu tangan sekuatnya", "Kedua kaki menyentuh tanah di luar/pada garis samping dan bola dilecutkan dari belakang kepala", "Kaki boleh melompat setinggi 30 cm", "Boleh dilempar langsung menjadi gol tanpa sentuhan", 1, "Kedua kaki harus tetap menyentuh tanah dan bola dipegang dengan kedua tangan melewati atas kepala."))
        quizzes.add(QuizQuestionEntity(0, 4, SportCategory.SEPAK_BOLA, "Bentuk posisi jari tangan penjaga gawang saat menangkap bola atas setinggi dada/wajah adalah menyerupai huruf...", "Huruf V", "Huruf W", "Huruf O", "Huruf X", 1, "Posisi W mengunci bagian belakang bola agar tidak lolos di antara kedua tangan."))
        quizzes.add(QuizQuestionEntity(0, 5, SportCategory.SEPAK_BOLA, "Seorang pemain penyerang dinyatakan offside apabila saat bola dioper kepadanya berada...", "Di daerah lapangannya sendiri", "Sejajar dengan pemain bertahan kedua terakhir", "Lebih dekat ke garis gawang lawan daripada bola dan pemain bertahan kedua terakhir lawan", "Menerima bola langsung dari lemparan ke dalam", 2, "Posisi offside terjadi di area lawan saat berada lebih dekat ke gawang dibanding pemain bertahan terakhir lawan selain kiper."))
        quizzes.add(QuizQuestionEntity(0, 5, SportCategory.SEPAK_BOLA, "Berapa lama durasi waktu normal pertandingan sepak bola resmi tanpa babak tambahan?", "2 x 40 menit", "2 x 45 menit", "4 x 15 menit", "2 x 50 menit", 1, "Waktu normal sepak bola adalah 2 babak x 45 menit."))
        quizzes.add(QuizQuestionEntity(0, 2, SportCategory.SEPAK_BOLA, "Gerakan menggiring bola dengan merubah arah secara cepat untuk mengelabui lawan disebut...", "Crossing", "Dribbling", "Tackling", "Intercepting", 1, "Dribbling adalah gerakan membawa dan menguasai bola sambil bergerak di lapangan."))
        quizzes.add(QuizQuestionEntity(0, 5, SportCategory.SEPAK_BOLA, "Hukuman kartu kuning kedua dalam satu pertandingan sepak bola mengakibatkan...", "Pemain mendapat peringatan keras lisan", "Pemain dikeluarkan (kartu merah) dan tim bermain dengan 10 orang", "Tim lawan mendapat 2 kali penalti", "Pertandingan dihentikan langsung", 1, "Kartu kuning kedua otomatis menjadi kartu merah dan pemain harus meninggalkan lapangan."))

        // -- Bola Basket Quizzes --
        quizzes.add(QuizQuestionEntity(0, 6, SportCategory.BOLA_BASKET, "Operan bola yang dilakukan dengan memantulkan bola ke lantai terlebih dahulu disebut...", "Chest pass", "Bounce pass", "Overhead pass", "Baseball pass", 1, "Bounce pass adalah operan pantul yang efektif menghindari sergapan tangan lawan."))
        quizzes.add(QuizQuestionEntity(0, 6, SportCategory.BOLA_BASKET, "Jarak titik pantul bola yang ideal pada teknik bounce pass adalah...", "1/3 jarak dari pengoper", "2/3 jarak menuju rekan penerima", "Tepat di depan kaki pengoper", "Di samping kaki lawan", 1, "Titik pantul sekitar 2/3 jarak agar bola naik setinggi pinggang rekan secara nyaman."))
        quizzes.add(QuizQuestionEntity(0, 7, SportCategory.BOLA_BASKET, "Gerakan berputar dengan salah satu kaki tetap bertumpu pada lantai sebagai poros disebut...", "Dribbling", "Pivot", "Lay-up", "Rebound", 1, "Pivot dilakukan untuk melindungi bola dari lawan tanpa melanggar traveling."))
        quizzes.add(QuizQuestionEntity(0, 7, SportCategory.BOLA_BASKET, "Pelanggaran yang terjadi ketika pemain berjalan atau berlari lebih dari 2 langkah tanpa mendribble bola adalah...", "Foul out", "Traveling / Walking", "Double dribble", "Three second violation", 1, "Traveling adalah pelanggaran melangkah tanpa memantulkan bola."))
        quizzes.add(QuizQuestionEntity(0, 8, SportCategory.BOLA_BASKET, "Dalam formula shooting B.E.E.F, huruf 'F' merupakan singkatan dari...", "Fast speed", "Follow through", "Foot work", "Forward jump", 1, "Follow through adalah gerakan kibasan pergelangan tangan leher angsa setelah bola dilepaskan."))
        quizzes.add(QuizQuestionEntity(0, 8, SportCategory.BOLA_BASKET, "Berapa jumlah langkah kaki legal saat melakukan lay-up shoot setelah memegang bola?", "1 langkah", "2 langkah", "3 langkah", "4 langkah", 1, "Irama lay-up adalah dua langkah berirama lalu melompat ke atas ke arah ring."))
        quizzes.add(QuizQuestionEntity(0, 9, SportCategory.BOLA_BASKET, "Gerakan memblokir posisi pemain lawan dengan membelakanginya saat berebut bola pantul disebut...", "Screen / Pick", "Box out", "Fast break", "Zone defense", 1, "Box out adalah kunci mengamankan posisi rebound di bawah ring."))
        quizzes.add(QuizQuestionEntity(0, 10, SportCategory.BOLA_BASKET, "Berapa batas waktu maksimal bagi tim penyerang untuk membawa bola dari backcourt ke frontcourt?", "24 detik", "8 detik", "5 detik", "3 detik", 1, "Aturan 8 detik mengharuskan tim melewati garis tengah menuju lapangan depan."))
        quizzes.add(QuizQuestionEntity(0, 10, SportCategory.BOLA_BASKET, "Tembakan yang berhasil masuk dari luar garis setengah lingkaran (arc) bernilai...", "1 poin", "2 poin", "3 poin", "4 poin", 2, "Tembakan di luar garis perimeter bernilai 3 poin (three-point shoot)."))
        quizzes.add(QuizQuestionEntity(0, 10, SportCategory.BOLA_BASKET, "Berapa jumlah maksimal personal foul yang boleh dilakukan seorang pemain sebelum foul out di standar FIBA?", "4 kali", "5 kali", "6 kali", "7 kali", 1, "Pada pertandingan standar FIBA, 5 kali pelanggaran pribadi mengakibatkan foul out."))

        // -- Bola Voli Quizzes --
        quizzes.add(QuizQuestionEntity(0, 11, SportCategory.BOLA_VOLI, "Teknik dasar yang paling dominan digunakan untuk menerima servis keras atau smash lawan adalah...", "Passing atas", "Passing bawah (Dig)", "Block ganda", "Smash silang", 1, "Passing bawah meredam momentum bola keras dan mengarahkannya ke toser."))
        quizzes.add(QuizQuestionEntity(0, 11, SportCategory.BOLA_VOLI, "Saat melakukan passing atas, perkenaan bola yang benar adalah pada...", "Telapak tangan", "Ruas-ruas jari tangan yang membentuk mangkuk", "Ujung kuku jari", "Pergelangan tangan", 1, "Passing atas menggunakan dorongan ruas-ruas jari lentur di depan atas dahi."))
        quizzes.add(QuizQuestionEntity(0, 12, SportCategory.BOLA_VOLI, "Servis yang bolanya langsung jatuh ke lapangan lawan dan menghasilkan poin tanpa bisa dikembalikan disebut...", "Fault", "Ace", "Deuce", "Spike", 1, "Service Ace adalah poin langsung yang dicetak melalui servis sempurna."))
        quizzes.add(QuizQuestionEntity(0, 13, SportCategory.BOLA_VOLI, "Pukulan keras menukik yang dilakukan di atas net untuk mematikan pertahanan lawan disebut...", "Set up", "Smash / Spike", "Lob", "Underhand serve", 1, "Smash adalah teknik penyerangan utama untuk mematikan reli lawan."))
        quizzes.add(QuizQuestionEntity(0, 14, SportCategory.BOLA_VOLI, "Tujuan utama dari teknik bendungan (block) di depan net adalah...", "Mengoper bola ke tosser", "Membendung atau meredam laju smash lawan", "Mempercepat rotasi pemain", "Melakukan tipuan servis", 1, "Block adalah benteng pertahanan pertama di atas bibir net."))
        quizzes.add(QuizQuestionEntity(0, 15, SportCategory.BOLA_VOLI, "Pemain bertahan khusus dalam bola voli yang memakai seragam berbeda warna dan tidak boleh melakukan smash/servis adalah...", "Tosser / Setter", "Libero", "Spiker", "Universal", 1, "Libero adalah pemain spesialis pertahanan baris belakang."))
        quizzes.add(QuizQuestionEntity(0, 15, SportCategory.BOLA_VOLI, "Arah perputaran (rotasi) posisi pemain dalam bola voli ketika merebut hak servis adalah...", "Berlawanan arah jarum jam", "Searah jarum jam (clockwise)", "Bebas sesuai keinginan kapten", "Maju mundur acak", 1, "Rotasi pemain selalu bergerak 1 posisi searah putaran jarum jam."))
        quizzes.add(QuizQuestionEntity(0, 15, SportCategory.BOLA_VOLI, "Berapa jumlah sentuhan maksimal yang boleh dilakukan satu tim sebelum bola harus diseberangkan ke lapangan lawan?", "2 sentuhan", "3 sentuhan", "4 sentuhan", "5 sentuhan", 1, "Setiap tim maksimal menyentuh bola 3 kali (biasanya dig - set - spike)."))
        quizzes.add(QuizQuestionEntity(0, 12, SportCategory.BOLA_VOLI, "Kesalahan kaki yang menginjak garis belakang saat memukul servis disebut...", "Centerline fault", "Foot fault", "Net violation", "Rotation fault", 1, "Foot fault terjadi jika kaki menyentuh garis baseline sebelum bola dipukul servis."))
        quizzes.add(QuizQuestionEntity(0, 11, SportCategory.BOLA_VOLI, "Pemain yang bertugas sebagai pengatur serangan dan memberikan umpan manja kepada spiker adalah...", "Libero", "Tosser / Setter", "Blocker", "Server", 1, "Setter bertugas membagi variasi bola umpan untuk penyerang."))

        // -- Atletik Quizzes --
        quizzes.add(QuizQuestionEntity(0, 16, SportCategory.ATLETIK, "Nomor lari yang termasuk dalam kategori lari jarak pendek (sprint) adalah...", "800m, 1500m, 3000m", "100m, 200m, 400m", "5000m dan 10000m", "Maraton 42,195 km", 1, "Sprint nomor resmi olimpiade adalah 100m, 200m, dan 400m."))
        quizzes.add(QuizQuestionEntity(0, 17, SportCategory.ATLETIK, "Jenis start yang digunakan untuk pelari pada nomor lari jarak pendek adalah...", "Start berdiri", "Start jongkok (Crouch start)", "Start melayang", "Start kombinasi", 1, "Lari jarak pendek selalu menggunakan start jongkok dengan bantuan starting block."))
        quizzes.add(QuizQuestionEntity(0, 17, SportCategory.ATLETIK, "Pada aba-aba 'Siap' dalam start jongkok, posisi pinggul pelari harus...", "Diturunkan menyentuh tumit", "Diangkat sedikit lebih tinggi dari bahu", "Sejajar dengan lutut kaki", "Digerakkan ke kiri dan ke kanan", 1, "Pinggul terangkat menciptakan kemiringan badan siap meluncur eksplosif ke depan."))
        quizzes.add(QuizQuestionEntity(0, 18, SportCategory.ATLETIK, "Teknik penyerahan tongkat estafet di mana penerima TIDAK menoleh ke belakang disebut cara...", "Visual", "Non-visual", "Blind pass", "Direct touch", 1, "Non-visual digunakan pada lari 4x100m agar penerima tetap berlari kecepatan maksimal."))
        quizzes.add(QuizQuestionEntity(0, 18, SportCategory.ATLETIK, "Zona pergantian tongkat estafet yang sah memiliki panjang lintasan sekitar...", "5 meter", "20-30 meter", "50 meter", "100 meter", 1, "Pergantian tongkat harus diselesaikan di dalam wissel zone sepanjang 20-30 meter."))
        quizzes.add(QuizQuestionEntity(0, 20, SportCategory.ATLETIK, "Urutan 4 tahapan gerakan yang benar dalam lompat jauh adalah...", "Melayang - Awalan - Tolakan - Mendarat", "Awalan - Tolakan - Melayang - Mendarat", "Tolakan - Mendarat - Awalan - Melayang", "Awalan - Mendarat - Tolakan - Melayang", 1, "Fase lompat jauh dimulai dari lari awalan, tolakan kaki di papan, melayang di udara, dan mendarat di bak pasir."))
        quizzes.add(QuizQuestionEntity(0, 20, SportCategory.ATLETIK, "Tolakan pada lompat jauh dinyatakan diskualifikasi (foul) apabila...", "Menolak menggunakan satu kaki", "Ujung sepatu pelompat melebihi garis batas plastisin papan tumpu", "Mendarat dengan kedua kaki", "Melompat lebih dari 6 meter", 1, "Menginjak garis plastisin di depan papan tumpuan dinyatakan tidak sah (No jump)."))
        quizzes.add(QuizQuestionEntity(0, 21, SportCategory.ATLETIK, "Gaya lompat tinggi di mana posisi atlet melewati mistar dengan membelakangi mistar dinamakan gaya...", "Gaya Gunting (Scissors)", "Gaya Fosbury Flop", "Gaya Guling Perut (Straddle)", "Gaya Jongkok", 1, "Fosbury Flop adalah gaya membelakangi mistar yang diciptakan oleh Dick Fosbury."))
        quizzes.add(QuizQuestionEntity(0, 22, SportCategory.ATLETIK, "Pada tolak peluru, gaya menolak peluru yang diawali dengan posisi badan MEMBELAKANGI arah tolakan adalah gaya...", "Gaya Ortodoks", "Gaya O'Brien (Glide)", "Gaya Western Roll", "Gaya Rotasi Diskus", 1, "Gaya O'Brien meluncur dengan awalan membelakangi sektor tolakan."))
        quizzes.add(QuizQuestionEntity(0, 24, SportCategory.ATLETIK, "Perbedaan mendasar antara gerak jalan cepat dan lari adalah pada jalan cepat...", "Tidak ada fase melayang, salah satu kaki selalu kontak dengan tanah", "Lutut selalu ditekuk 90 derajat", "Boleh melayang di udara setiap langkah", "Kecepatannya harus melebihi sprint", 0, "Jalan cepat mewajibkan kontak kaki terpelihara tanpa terputus dengan permukaan tanah."))

        // -- Penanganan Cedera Quizzes --
        quizzes.add(QuizQuestionEntity(0, 25, SportCategory.PENANGANAN_CEDERA, "Tujuan utama dilakukannya pemanasan dinamis sebelum berolahraga adalah...", "Membuat tubuh cepat lelah", "Meningkatkan suhu tubuh, elastisitas otot, dan mencegah cedera", "Menghentikan peredaran darah", "Mengurangi asupan oksigen", 1, "Pemanasan melumasi sendi dan menyiapkan otot untuk beban aktivitas tinggi."))
        quizzes.add(QuizQuestionEntity(0, 26, SportCategory.PENANGANAN_CEDERA, "Dalam prinsip pertolongan pertama R.I.C.E, huruf 'I' merupakan singkatan dari...", "Immobilize", "Ice (Kompres Dingin)", "Injection", "Intense massage", 1, "Ice adalah kompres dingin untuk menyempitkan pembuluh darah dan mengurangi pembengkakan."))
        quizzes.add(QuizQuestionEntity(0, 26, SportCategory.PENANGANAN_CEDERA, "Berapa lama durasi aman kompres es yang dianjurkan pada cedera pergelangan kaki akut?", "15-20 menit dengan lapisan kain pelindung", "2 jam terus menerus tanpa henti", "30 detik saja", "1 menit langsung dengan es batu keras", 0, "Kompres es 15-20 menit dibalut kain mencegah radang dingin (frostbite) pada jaringan kulit."))
        quizzes.add(QuizQuestionEntity(0, 26, SportCategory.PENANGANAN_CEDERA, "Tindakan yang DILARANG keras dilakukan dalam 48 jam pertama pada cedera keseleo akut adalah...", "Mengistirahatkan kaki", "Melakukan pemijatan keras (massage) dan kompres panas", "Meninggikan posisi kaki saat tidur", "Membalut tekan dengan perban elastis", 1, "Pijat dan kompres panas pada fase akut memperlebar pembuluh darah dan memperparah pembengkakan."))
        quizzes.add(QuizQuestionEntity(0, 27, SportCategory.PENANGANAN_CEDERA, "Pertolongan pertama yang paling tepat saat terjadi kram otot pada betis adalah...", "Memukul-mukul betis dengan keras", "Meregangkan otot betis secara perlahan dengan mendorong telapak kaki ke atas (dorsofleksi)", "Mengompres air mendidih", "Memaksa atlet langsung lari kembali", 1, "Peregangan lembut dorsofleksi mengembalikan panjang serat otot yang memendek akibat kram."))
        quizzes.add(QuizQuestionEntity(0, 28, SportCategory.PENANGANAN_CEDERA, "Cedera yang terjadi akibat robekan atau regangan berlebih pada LIGAMEN persendian disebut...", "Strain", "Sprain (Keseleo)", "Fraktur", "Dislokasi", 1, "Sprain adalah cedera pada ligamen sendi, sedangkan Strain pada otot/tendon."))
        quizzes.add(QuizQuestionEntity(0, 29, SportCategory.PENANGANAN_CEDERA, "Cairan yang paling aman dan tepat digunakan untuk mencuci luka lecet yang kotor berpasir adalah...", "Alkohol 95% pekat", "Cairan steril infus NaCl 0.9% atau air bersih mengalir", "Minyak goreng", "Pasta gigi", 1, "NaCl steril atau air bersih mengalir membersihkan kotoran tanpa merusak sel epitel baru."))
        quizzes.add(QuizQuestionEntity(0, 26, SportCategory.PENANGANAN_CEDERA, "Tujuan dari langkah 'Elevation' (meninggikan bagian cedera di atas jantung) adalah...", "Menaikkan tekanan darah di kaki", "Membantu aliran balik darah vena dan cairan getah bening untuk mengurangi bengkak", "Membuat kaki kaku", "Menghentikan pernapasan", 1, "Elevasi memanfaatkan gravitasi untuk mengurangi penumpukan cairan edema di area cedera."))
        quizzes.add(QuizQuestionEntity(0, 27, SportCategory.PENANGANAN_CEDERA, "Penyebab utama timbulnya kram otot saat berolahraga di cuaca panas adalah...", "Kelebihan gula darah", "Dehidrasi dan ketidakseimbangan cairan elektrolit tubuh", "Kekurangan istirahat bernapas", "Terlalu banyak minum susu", 1, "Kehilangan natrium, kalium, dan magnesium lewat keringat memicu spasme otot."))
        quizzes.add(QuizQuestionEntity(0, 29, SportCategory.PENANGANAN_CEDERA, "Manakah kondisi darurat berikut yang mewajibkan atlet SEGERA dirujuk ke rumah sakit / IGD?", "Luka lecet kecil di jari tangan", "Pingsan/hilang kesadaran setelah benturan kepala atau patah tulang tampak", "Keringat keluar banyak", "Otot betis terasa sedikit pegal setelah lari", 1, "Penurunan kesadaran dan patah tulang adalah tanda darurat medis yang memerlukan penanganan dokter."))

        database.quizDao().insertQuizzes(quizzes)

        // 6. Seed Initial Progress for student (Ilham)
        val initialProgress = listOf(
            ProgressEntity(0, 1, 1, SportCategory.SEPAK_BOLA, 100, LearningStatus.COMPLETED),
            ProgressEntity(0, 1, 2, SportCategory.SEPAK_BOLA, 100, LearningStatus.COMPLETED),
            ProgressEntity(0, 1, 6, SportCategory.BOLA_BASKET, 80, LearningStatus.IN_PROGRESS),
            ProgressEntity(0, 1, 11, SportCategory.BOLA_VOLI, 100, LearningStatus.COMPLETED),
            ProgressEntity(0, 1, 16, SportCategory.ATLETIK, 45, LearningStatus.IN_PROGRESS),
            ProgressEntity(0, 1, 25, SportCategory.PENANGANAN_CEDERA, 30, LearningStatus.IN_PROGRESS)
        )
        for (p in initialProgress) {
            database.progressDao().insertOrUpdateProgress(p)
        }

        // 7. Seed Initial Notifications
        val notifications = listOf(
            NotificationEntity(
                0, 1, "Selamat Datang di Smart Sport Learning!",
                "Mulai jelajahi 5 cabang olahraga seru dan tingkatkan kompetensi PJOK kamu.",
                NotificationType.INFO, false
            ),
            NotificationEntity(
                0, 1, "Badge Baru Terbuka!",
                "Selamat! Kamu telah meraih badge 'Football Expert' setelah menyelesaikan materi sepak bola.",
                NotificationType.BADGE_UNLOCKED, false
            ),
            NotificationEntity(
                0, 1, "Lanjutkan Belajar Atletik",
                "Progress materi Atletik kamu sudah mencapai 45%. Selesaikan modul Lari Estafet sekarang!",
                NotificationType.REMINDER, false
            )
        )
        for (n in notifications) {
            database.notificationDao().insertNotification(n)
        }
    }
}
