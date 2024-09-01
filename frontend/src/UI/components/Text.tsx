import styled from "styled-components";
import { ThemeColors, ThemeFontScaling } from "../theme/ThemeTypes";
import { ReactNode } from "react";

const StyledText = styled.p<{
  $fontSize: ThemeFontScaling;
  $color: ThemeColors;
  $bold: boolean;
}>`
  font-size: ${(props) => props.theme.fontSize[props.$fontSize]};
  color: ${(props) => props.theme.colors[props.$color]};
  font-weight: ${(props) => (props.$bold ? "bold" : "unset")};
`;

export function TextView({
  fontSize,
  children,
  color,
  bold = false,
  className,
}: TextViewProps) {
  return (
    <StyledText
      className={className}
      $fontSize={fontSize}
      $color={color}
      $bold={bold}
    >
      {children}
    </StyledText>
  );
}

interface TextViewProps {
  fontSize: ThemeFontScaling;
  color: ThemeColors;
  children: ReactNode;
  bold?: boolean;
  className?: string;
}
