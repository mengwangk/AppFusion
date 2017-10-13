// Extra variables that live on Global that will be replaced by webpack DefinePlugin
declare var _UPLOAD_URL_: string;

interface GlobalEnvironment {
  _UPLOAD_URL_;
}
