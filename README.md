# Feature-Memory Installer

Taşınabilir kurulum paketi: Claude Code projelerine **feature-memory** sistemini (10 slash command + 3 hook + script altyapısı + şablonlar) tek komutla kurar.

## İçerik

```
feature-memory-installer/
├── install.sh          ← kurulum scripti
├── README.md
└── payload/
    ├── CLAUDE.md       ← workflow dokümantasyonu (.claude/CLAUDE.md olarak kopyalanır)
    ├── commands/       ← /feature-* slash komutları (10 adet)
    ├── hooks/          ← session-start.sh, stop.sh, pre-compact.sh
    ├── scripts/        ← init, list, resume, set-active vb.
    └── templates/      ← feature.md / notes.md şablonları
```

## Kurulum

Hedef projenin **kök dizininde**:

```bash
/path/to/feature-memory-installer/install.sh
```

veya hedef yolu açıkça vererek:

```bash
./install.sh /path/to/another-project
```

### Bağımlılıklar
- `bash` (macOS / Linux)
- `jq` (önerilir — mevcut `settings.json` ile hook'ları akıllıca birleştirir; yoksa elle eklemen istenir)

### Ne yapar

1. `<proj>/.claude/{commands,hooks,scripts,templates,features,backups}` ağacını oluşturur
2. Payload'ı kopyalar; hook + script dosyalarını executable yapar
3. `.claude/settings.json` varsa **yedekler ve hook'ları merge eder** (jq ile); yoksa yeni oluşturur
4. `.claude/CLAUDE.md` yoksa workflow dokümanını yazar (varsa dokunmaz)
5. `.gitignore`'a `.claude/features/.active` ve `.claude/backups/` ekler

### Kaldırma

```bash
./install.sh --uninstall              # mevcut dizin
./install.sh --uninstall /path/proj
```

`features/` klasörü (mevcut feature kayıtların) **silinmez**. `settings.json` da elle temizlenmelidir (yedeği `.bak.<timestamp>` olarak yanında durur).

## Kullanım — kuruluştan sonra

```
/feature-start <ad>          → feature.md + notes.md oluşturur, aktif pointer set eder
/feature-status              → aktif feature için ilerleme raporu
/feature-note [tag] <metin>  → notes.md'ye tek satır
/feature-decision <metin>    → feature.md Decisions tablosuna
/feature-finding <metin>     → feature.md Findings bölümüne
/feature-compact             → notes.md dedupe + arşiv (önce dry-run)
/feature-pause | /feature-resume | /feature-list | /feature-end
```

Yeni chat / `/clear` / compaction sonrası SessionStart hook aktif feature'ı otomatik yükler.

## Hook davranışı

| Hook | Tetikleyici | İşlevi |
|---|---|---|
| `session-start.sh` | Yeni chat / `/clear` / compaction sonrası | Aktif feature'ın `feature.md`'sini ve `notes.md` son 50 satırını + tüm `[invariant]` / `[criteria]` satırlarını chat'e enjekte eder |
| `stop.sh` | Session bitiminde | Aktif feature varsa propose-confirm akışı başlatır (DECISIONS / FINDINGS / ERRORS taslağı) — kullanıcı `y` derse Claude `feature.md`'ye yazar |
| `pre-compact.sh` | Context compaction'dan önce | Transcript yedeği alır + `notes.md`'yi STICKY bloğunda görünür kılar (compaction sonrası kaybolmaz) |

### Proje-özel uzantı noktası

`stop.sh`, opsiyonel olarak `.claude/hooks/post-stop.local.sh` dosyasını fire-and-forget çalıştırır (varsa ve executable ise). Buraya proje-özel pattern scanner / lint / metric script'i koyabilirsiniz; output yutulur, ana flow'u bozmaz.

## Notlar

- Bu paket **temamen generic**'tir; proje-spesifik (IBAM vb.) referans **kalmamıştır**.
- `.claude/CLAUDE.md` **yalnızca yoksa** yazılır; mevcut bir CLAUDE.md'nin üstüne yazmaz.
- Checkpoint sistemi (auto-checkpoint, save/restore-checkpoint) ayrı bir konsept olduğu için bu paket içinde **yok**; saf feature-memory bileşenleri vardır.
