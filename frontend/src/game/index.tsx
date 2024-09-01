import { Outlet } from "react-router-dom";
import { Header } from "../UI/components/Header";
import { useProfile } from "../hooks/useProfile";
import { TextView } from "../UI/components/Text";
import { If } from "../UI/components/If";
import { Container } from "../UI/components/Container";
export function Game() {
  const { name } = useProfile();

  return (
    <Container>
      <If condition={name == ""}>
        <TextView fontSize="XL2" color="onBackground" bold>
          Connecting...
        </TextView>
      </If>
      <If condition={name != ""}>
        <Header name={name} />
        <Outlet />
      </If>
    </Container>
  );
}
