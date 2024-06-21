{pkgs ? import <nixpkgs> {} }: pkgs.mkShell {
    nativeBuildInputs = with pkgs.buildPackages; [
        jdk17
    ];

    env = {
        JAVA_HOME = "${pkgs.jdk17}/lib/openjdk";
    };
}
