PDF_FLAGS = --fontsize 10.0 \
			--linkstyle plain \
			--linkcolor blue \
			--embedfonts \
			--no-jpeg \
			--numbered \
			--title \
			--titlefile pages/cover.html \
			--headfootsize 9.0 \
			--footer "c 1" 
CALIBRE_FLAGS = --authors "Zef Hemel" \
								--title "mobl: An Incomplete Guide" \
								--cover img/cover.png \
								--level1-toc="//h:h1" \
								--use-auto-toc
								#--level1-toc="//*[name()='h1']" \
								#--level2-toc="//*[name()='h2']" \

HTML = $(MD:.md=.html)

MD = introduction.md \
		 setup.md \
		 tutorial.md \
		 data.md \
		 types.md \
		 ui.md \
		 script.md \
		 settings.md

dist: dist/moblguide.html dist/moblguide.pdf dist/moblguide.mobi dist/moblguide.epub

init:
	mkdir -p dist

dist/moblguide.html: init pages/head.html pages/tail.html $(HTML)
	@echo "\n... generating $@"
	cat pages/head.html $(HTML) pages/tail.html > dist/moblguide.html
	cp -r img dist/

dist/moblguide.pdf: dist/moblguide.html
	@echo "\n... generating $@"
	htmldoc dist/moblguide.html $(PDF_FLAGS) --outfile $@


%.html: %.md
	ronn --pipe --fragment $< \
		| sed -E 's/<h1>([^ ]+) - /<h1>/' \
		> $@

dist/moblguide.mobi:
	@echo "\n... generating $@"
	ebook-convert dist/moblguide.html dist/moblguide.mobi --output-profile kindle $(CALIBRE_FLAGS)

dist/moblguide.epub:
	@echo "\n... generating $@"
	ebook-convert dist/moblguide.html dist/moblguide.epub $(CALIBRE_FLAGS)

clean:
	rm -f *.html
	rm -rf dist

