#@namespace scala com.itiancai.passport.thrift
struct Tweet {
    1: required i32 userId;
    2: required string userName;

}

service PassportService {
   string hi(1: string word)
}