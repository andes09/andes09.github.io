interface whatClientExpects {
  void request();
}

class existingCode {
  void specificRequest() {
    print("Adaptee: doing its own thing");
  }
}
class Adapter implements whatClientExpects {
  private existingCode ec;

  Adapter(existingCode ec) {
    this.ec = ec;
  }

  @Override
  void request() {
    // Translate Target request to Adaptee call
    ec.specificRequest();
  }
}