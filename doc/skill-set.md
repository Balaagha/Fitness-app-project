# Claude Code — Skill & Komanda Rehberi

Bu projede aktif olan tüm skill'ler, plugin'ler ve komutlar burada özetlenmiştir.

---

## Hızlı Başvuru

| Ne yapmak istiyorum | Komut |
|---------------------|-------|
| Feature spec yaz | `/write-spec <fikir>` |
| Rakipleri karşılaştır | `/competitive-brief <rakipler>` |
| Roadmap güncelle | `/roadmap-update <değişiklik>` |
| Beyin fırtınası yap | `/brainstorm <konu>` |
| Yeni feature başlat | `/feature-start <ad>` |
| Mevcut feature durumu | `/feature-status` |
| Sprint planla | `/sprint-planning [tarih]` |
| Yeni skill oluştur | skill-creator tetikleyicilerinden birini yaz |

---

## 1. Product Management Plugin

**Plugin:** `product-management@knowledge-work-plugins` — aktif

Ürün yönetimi iş akışlarını Claude Code içinden, terminal'dan çıkmadan yürütür. CLAUDE.md'deki mimari, stack ve rekabet bilgilerini otomatik bağlam olarak kullanır.

MCP entegrasyonları (ek auth gerekli): Linear, Notion, Jira, ClickUp, Asana, Slack, Figma, Amplitude, Intercom, Fireflies, Monday.com, Pendo.

### Komutlar

#### `/write-spec`
Belirsiz bir fikirden yapılandırılmış PRD/feature spec üretir.
- Problem → Hedef/Kapsam dışı → Kullanıcı hikayeleri → Başarı metrikleri → Faz planı → Kabul kriterleri
```
/write-spec onboarding ekranı — hedef seçimi ve profil kurulumu
/write-spec workout session — hareket listesi, timer, 3D model
```

#### `/competitive-brief`
Rakip analizi belgesi oluşturur.
- Feature matrisi → Güçlü/zayıf yönler → Farklılaşma fırsatları → Öneriler
```
/competitive-brief BetterMe, MyFitnessPal — Azerbaycan pazarı için
```

#### `/roadmap-update`
Mevcut roadmap'i yeniden önceliklendirir veya timeline'ı değiştirir.
```
/roadmap-update AI video üretimini MVP'den faz 2'ye taşı
```

#### `/stakeholder-update`
Hedef kitleye göre (yönetim, mühendislik, müşteri) durum güncellemesi yazar.
```
/stakeholder-update Sprint 1 tamamlandı — yatırımcı güncellemesi
```

#### `/synthesize-research`
Görüşme notları, anketler ve geri bildirimden yapılandırılmış içgörüler çıkarır.
```
/synthesize-research Azerbaycanlı kullanıcıların ev egzersizi ile ilgili sorunları
```

#### `/metrics-review`
Ürün metriklerini trend analiziyle inceler, aksiyon önerir.
```
/metrics-review MVP sonrası ilk 2 hafta — retention ve aktivasyon
```

#### `/sprint-planning`
Sprint kapsamı, kapasite, P0 vs. esnek iş ve carryover kararları.
```
/sprint-planning Sprint 1 — onboarding + profil (12–26 Mayıs)
```

#### `/brainstorm`
Bir ürün fikri veya stratejik soru üzerinde düşünce ortağı olarak çalışır.
```
/brainstorm Azerbaycan pazarı için freemium model nasıl kurulmalı?
```

---

## 2. Feature-Memory Komutları

**Kaynak:** Proje seviyesi — `.claude/commands/` — her zaman aktif

Çalışma bağlamını oturumlar arasında taşıyan sistem. Her iş için `feature.md` + `notes.md` oluşturur.

| Komut | Ne yapar |
|-------|----------|
| `/feature-start <ad>` | Yeni feature başlatır — `feature.md` + `notes.md` oluşturur |
| `/feature-status` | Aktif feature'nin durumu (progress bar, kararlar, notlar) |
| `/feature-decision "<metin>"` | `feature.md` Kararlar tablosuna el ile karar ekler |
| `/feature-finding "<metin>"` | `feature.md` Bulgular bölümüne bulgu ekler |
| `/feature-note [etiket] "<metin>"` | `notes.md`'e tek satırlık not ekler |
| `/feature-compact` | `notes.md`'i arşivler ve tekrarları temizler |
| `/feature-pause` | Aktif pointer'ı siler (dosyalar kalır) |
| `/feature-resume <ad>` | Duraklatılmış feature'yi yeniden etkinleştirir |
| `/feature-list` | Tüm feature'leri listeler (aktif/duraklatılmış/arşiv) |
| `/feature-end` | Feature'yi tamamlar ve arşivler |

**Notes etiketleri:** `[impl]` `[gotcha]` `[criteria]` `[refs]` `[invariant]`

---

## 3. Skill Creator Plugin

**Plugin:** `skill-creator@claude-plugins-official` — aktif

Sıfırdan yeni skill oluşturur, mevcut skill'leri günceller ve performansını ölçer.

Tetikleyiciler: "yeni skill oluştur", "bu skill'i güncelle", "skill performansını ölç"

---

## 4. YouTube Haftalık Özet Skill

**Kapsam:** Kullanıcı seviyesi — aktif

YouTube kanal verilerinden profesyonel rapor oluşturur: video performansı, yorum sentiment analizi, görseller, SWOT. Türkçe veya İngilizce çalışır.

Tetikleyiciler: "bu haftaki videolarımı analiz et", "YouTube raporumu oluştur", "kanal performansıma bak"

---

## 5. Pasif Plugin'ler (etkinleştirilebilir)

```bash
claude plugin enable <plugin-adı>@claude-plugins-official
```

| Plugin | Ne yapar | Komut |
|--------|----------|-------|
| `frontend-design` | Production kaliteli UI bileşenleri oluşturur, jenerik AI estetiğinden kaçınır | `claude plugin enable frontend-design@claude-plugins-official` |
| `claude-automation-recommender` | Codebase'i analiz edip hook/skill/MCP önerir | `claude plugin enable claude-code-setup@claude-plugins-official` |
| `claude-md-improver` | `CLAUDE.md` dosyalarını denetler ve geliştirir | `claude plugin enable claude-md-management@claude-plugins-official` |
| `Notion` (4 skill) | Araştırma belgeleme, toplantı hazırlığı, bilgi yakalama, spec→task dönüşümü | `claude plugin enable Notion@claude-plugins-official` |

> Notion için ön koşul: `claude mcp add --transport http notion https://mcp.notion.com/mcp`
