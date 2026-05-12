# The Agentic Startup — Türkçe Kullanım Kılavuzu

> **Sürüm**: v3.7.0 (2026-05-07)  
> **Kaynak**: https://github.com/rsmdt/the-startup  
> **Yerel kopyası**: `temp/rsmdt-the-startup/`

---

## Uyumluluk Raporu

### Fitness App Projesiyle Uyumlu mu?

**Sonuç: ✅ Tamamen Uyumlu — Kurulum Önerilir**

| Kriter | Durum | Detay |
|--------|-------|-------|
| Claude Code sürümü | ✅ | Gerekli: v2.0+, Mevcut: v2.1.138 |
| Marketplace desteği | ✅ | `claude plugin marketplace` komutu aktif |
| `jq` kurulu mu? | ✅ | v1.8.1 mevcut |
| feature-memory çakışması | ✅ Yok | Farklı dizinler (`.claude/` vs `.start/`) |
| Slash command çakışması | ✅ Yok | `/feature-*` vs `/specify`, `/implement`, vs. |
| Proje aşaması uyumu | ✅ İdeal | Sıfırdan başlayan proje için en verimli zaman |

---

## Framework Nedir?

The Agentic Startup, Claude Code'u bir startup ekibi gibi çalıştıran **spec-driven (spesifikasyon güdümlü) bir geliştirme çerçevesidir**. Önce kapsamlı spesifikasyon yaz, sonra paralel uzman agent'larla uygula.

Temel felsefe: **Önce ölç, sonra kes.** Kod yazmadan önce ne yapacağını netleştirmek, implementation'daki değişiklik maliyetini dramatik biçimde düşürür.

---

## Kurulum

### Gereksinimler
- Claude Code v2.0+
- `jq` (macOS için: `brew install jq`)
- `curl`

### Hızlı Kurulum

```bash
curl -fsSL https://raw.githubusercontent.com/rsmdt/the-startup/main/install.sh | sh
```

Bu komut şunları yapar:
1. `start@the-startup` ve `team@the-startup` plugin'lerini kurar
2. Varsayılan output stilini "The Startup" olarak ayarlar
3. Terminal statusline'ını yapılandırır

### Manuel Test (Önce Denemek İçin)

```bash
# Yerel kopya üzerinden kur
claude plugin install ./temp/rsmdt-the-startup/plugins/start
claude plugin install ./temp/rsmdt-the-startup/plugins/team
```

---

## 10 Ana Komut

### SETUP Aşaması

| Komut | Amaç |
|-------|------|
| `/constitution` | Proje genelinde kural seti oluştur (isteğe bağlı ama önerilir) |

### BUILD Aşaması (Ana Akış)

| Komut | Amaç |
|-------|------|
| `/specify <açıklama>` | Spesifikasyon oluştur (gereksinimler + çözüm tasarımı + parçalama) |
| `/validate <spec-id>` | Spesifikasyon kalitesini kontrol et |
| `/implement <spec-id>` | Spesifikasyonu koda dönüştür |
| `/test` | Testleri çalıştır, sahipliği zorla |
| `/review` | Çoklu agent kod incelemesi |
| `/document` | Dokümantasyon oluştur/senkronize et |

### MAINTAIN Aşaması

| Komut | Amaç |
|-------|------|
| `/analyze` | Mevcut kodu keşfet, pattern'ları bul |
| `/refactor` | Kodu iyileştir (davranışı koru) |
| `/debug` | Hataları kök neden analiziyle düzelt |

---

## Temel Akış: Adım Adım

### 1. Spesifikasyon Oluştur

```
/specify Kullanıcı kaydı ve JWT tabanlı oturum açma sistemi ekle
```

Claude şunları yapar:
- Projeyi araştırır
- Sorular sorar
- `.start/specs/001-kullanici-kaydi/` altında 2 dosya oluşturur:
  - `requirements.md` — Ne yapılacak ve neden
  - `solution.md` — Teknik tasarım
- Karmaşıklığa göre ek parçalama yapar (aşağıdaki tier sistemi)

### 2. Tier Sistemi (Karmaşıklık Sınıflaması)

`/specify` adım 6'da otomatik olarak en uygun tier'ı önerir:

| Tier | Ne Zaman | Oluşturulan Dosya | Implementation |
|------|----------|-------------------|---------------|
| **Direct** | Düzeltme, refactor, tek kabul kriterli özellik | Yok — requirements + solution yeterli | Hafif, 1–3 agent |
| **Incremental** | Tek özellik, 1–2 bileşen | `plan/README.md` + `plan/phase-N.md` | Aşama döngüsü, insanlı onay |
| **Factory** | Çok özellik, paralel iş | `manifest.md` + `units/` + `scenarios/` | Paralel agent'lar, bilgi bariyerleri |

### 3. Doğrula

```
/validate 001
```

3C çerçevesiyle kontrol eder:
- **Completeness** (Tamlık) — Tüm bölümler dolu mu?
- **Consistency** (Tutarlılık) — Çelişki var mı?
- **Correctness** (Doğruluk) — Gereksinimler test edilebilir mi?

### 4. Uygula

```
/implement 001
```

Tier'ı otomatik algılar ve uygun execution sub-skill'i çağırır.

### 5. İncele

```
/review
```

4 paralel uzman çalışır:
- 🔒 Güvenlik
- ⚡ Performans
- ✨ Kod kalitesi
- 🧪 Test kapsamı

---

## Output Stilleri

İki çalışma kişiliği arasında istediğin zaman geçiş yapabilirsin:

```
/output-style start:The Startup    # Yüksek enerji, hızlı teslimat
/output-style start:The ScaleUp    # Sakin, eğitici açıklamalar
```

**The Startup**: Y Combinator enerjisi. "Hadi hemen çıkaralım!" hissi. Kısa açıklamalar, yüksek tempo.

**The ScaleUp**: Mühendislik profesyonelliği. Her karar için "neden böyle yaptım" açıklamaları ekler — yeni bir kod tabanını öğrenirken idealdir.

---

## Feature-Memory Sistemi ile Birlikte Kullanım

Projende zaten kurulu olan feature-memory sistemiyle **tam uyumlu** çalışır. Çakışma yoktur:

| Sistem | Dizin | Amaç |
|--------|-------|------|
| feature-memory | `.claude/features/` | Oturumlar arası çalışma belleği |
| The Agentic Startup | `.start/specs/` + `docs/` | Spesifikasyon belgeleri |

**Önerilen kombinasyon:**

1. `/feature-start kullanici-auth` — Çalışma oturumu başlat
2. `/specify Kullanıcı kimlik doğrulama sistemi` — Spec oluştur
3. `/implement 001` — Kodu yaz
4. `/review` — İncele
5. `/feature-end` — Oturumu kapat, arşivle

---

## Fitness App İçin Önerilen Başlangıç Akışı

Proje henüz başlangıç aşamasında olduğu için bu framework'ü **şimdi kurmak idealdir** — çünkü:
- Stack kararları henüz verilmemiş (spec sistemi bu kararları kayıt altına alır)
- Kod tabanı temiz (constitution ile kural seti oluşturmak kolaydır)
- İlk özellikler spec'lenince tüm proje bu standarda göre büyür

### Sıralama

```
1. /constitution                          # Proje kuralları (dil, mimari, stil)
2. /specify Stack seçimi: React Native    # İlk büyük karar
3. /specify Veri modeli: Egzersiz takibi  # Temel domain
4. /specify Kullanıcı kaydı ve auth       # İlk özellik
```

---

## Önemli Notlar

### Context Limiti Aşılırsa

Büyük spesifikasyonlar context limitine ulaşabilir. Yeni oturum açıp kaldığın yerden devam et:

```
/specify 001    # Mevcut spec ID'sini ver — kaldığı yerden devam eder
/implement 001  # Aynı şekilde
```

### Spec Dosyaları Nerede?

```
.start/specs/
└── 001-ozellik-adi/
    ├── requirements.md
    ├── solution.md
    └── plan/           (Incremental tier seçildiyse)
        ├── README.md
        └── phase-1.md
```

### Herhangi Bir Planla Çalışır

`/specify` ile oluşturulmamış planlar da çalışır:

```
/implement path/to/migration-plan.md
```

---

## Statusline

Kurulumdan sonra terminal statusline'ında şu bilgiler görünür:

```
📁 ~/D/fitnessApp ⎇ main*  🤖 Sonnet 4.6  🧠 ⣿⣿⡇⠀⠀ 50%  🕐 15m  💰 $0.30
```

Konfigürasyon: `~/.config/the-agentic-startup/statusline.toml`

---

## Kısaca: Ne Zaman Hangi Komutu Kullan?

```
Yeni özellik mi?          → /specify → /validate → /implement → /review
Mevcut kodu anlama?       → /analyze
Kodu temizleme?           → /refactor
Hata var mı?              → /debug
Test çalıştırma?          → /test
PR'a hazır mı?            → /review
Dökümantasyon?            → /document
Proje kuralları?          → /constitution
```
