{ nixpkgs ? /etc/nixos/nixpkgs
, mobl ? { outPath = ./.; rev = "1234"; }
}:

let
  pkgs = import nixpkgs { system = "x86_64-linux" ; };
  jobs = {
    manual = pkgs.stdenv.mkDerivation {
      name = "mobl-manual-${mobl.rev}";
      src = mobl;
      buildInputs = with pkgs; [perl htmldoc rubygems calibre];
      buildCommand = ''
        unpackPhase

        export HOME=`pwd`
        export GEM_HOME=`pwd`/gem
        export PATH=$PATH:$GEM_HOME/bin
        export RUBYLIB=${pkgs.rubygems}/lib

        gem install ronn

        cd $sourceRoot/manual
        make 

        ensureDir $out/nix-support
        cp -vR dist $out/
        echo "doc manual $out/dist/moblguide.html" >> $out/nix-support/hydra-build-products
        echo "doc-pdf manual $out/dist/moblguide.pdf" >> $out/nix-support/hydra-build-products
        echo "doc mobi $out/dist/moblguide.mobi" >> $out/nix-support/hydra-build-products
        echo "doc epub $out/dist/moblguide.epub" >> $out/nix-support/hydra-build-products
      '';
      __noChroot = true;
    };
  };

in jobs

