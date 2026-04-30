const char *emulator_tag = "[cpuemu][v0.01|fly]";


void log(const char *message) {
    printf("%s::", emulator_tag);
    printf("%s", message);
    printf("\n");
}